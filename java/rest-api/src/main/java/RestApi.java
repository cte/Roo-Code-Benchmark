import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

class RestApi {
    private final List<User> users;

    RestApi() {
        this.users = new ArrayList<>();
    }

    RestApi(User... users) {
        this.users = new ArrayList<>(Arrays.asList(users));
    }

    String get(String url) {
        if ("/users".equals(url)) {
            return getAllUsers();
        }
        throw new UnsupportedOperationException("Unsupported GET endpoint: " + url);
    }

    String get(String url, JSONObject payload) {
        if ("/users".equals(url)) {
            return getSpecificUsers(payload);
        }
        throw new UnsupportedOperationException("Unsupported GET endpoint: " + url);
    }

    String post(String url, JSONObject payload) {
        if ("/add".equals(url)) {
            return addUser(payload);
        } else if ("/iou".equals(url)) {
            return createIou(payload);
        }
        throw new UnsupportedOperationException("Unsupported POST endpoint: " + url);
    }

    private String getAllUsers() {
        JSONObject response = new JSONObject();
        JSONArray usersArray = new JSONArray();
        
        for (User user : users) {
            usersArray.put(convertUserToJson(user));
        }
        
        response.put("users", usersArray);
        return response.toString();
    }

    private String getSpecificUsers(JSONObject payload) {
        JSONObject response = new JSONObject();
        JSONArray usersArray = new JSONArray();
        
        JSONArray requestedUsers = payload.getJSONArray("users");
        Set<String> requestedUserNames = new HashSet<>();
        
        for (int i = 0; i < requestedUsers.length(); i++) {
            requestedUserNames.add(requestedUsers.getString(i));
        }
        
        List<User> filteredUsers = users.stream()
                .filter(user -> requestedUserNames.contains(user.name()))
                .sorted(Comparator.comparing(User::name))
                .collect(Collectors.toList());
        
        for (User user : filteredUsers) {
            usersArray.put(convertUserToJson(user));
        }
        
        response.put("users", usersArray);
        return response.toString();
    }

    private String addUser(JSONObject payload) {
        String userName = payload.getString("user");
        User newUser = User.builder().setName(userName).build();
        users.add(newUser);
        return convertUserToJson(newUser).toString();
    }

    private String createIou(JSONObject payload) {
        String lenderName = payload.getString("lender");
        String borrowerName = payload.getString("borrower");
        double amount = payload.getDouble("amount");
        
        User lender = findUserByName(lenderName);
        User borrower = findUserByName(borrowerName);
        
        // Process the IOU and get updated users
        List<User> updatedUsers = processIou(lender, borrower, amount);
        User updatedLender = updatedUsers.get(0);
        User updatedBorrower = updatedUsers.get(1);
        
        // Replace old users with updated ones
        replaceUser(lender, updatedLender);
        replaceUser(borrower, updatedBorrower);
        
        // Return the updated users
        JSONObject response = new JSONObject();
        JSONArray usersArray = new JSONArray();
        
        List<User> sortedUsers = Arrays.asList(updatedLender, updatedBorrower);
        sortedUsers.sort(Comparator.comparing(User::name));
        
        for (User user : sortedUsers) {
            usersArray.put(convertUserToJson(user));
        }
        
        response.put("users", usersArray);
        return response.toString();
    }

    private User findUserByName(String name) {
        return users.stream()
                .filter(user -> user.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + name));
    }

    private void replaceUser(User oldUser, User newUser) {
        int index = users.indexOf(oldUser);
        if (index != -1) {
            users.set(index, newUser);
        }
    }

    private List<User> processIou(User lender, User borrower, double amount) {
        // Create maps to track debts
        Map<String, Double> lenderOwes = new HashMap<>();
        Map<String, Double> lenderOwedBy = new HashMap<>();
        Map<String, Double> borrowerOwes = new HashMap<>();
        Map<String, Double> borrowerOwedBy = new HashMap<>();
        
        // Populate maps with existing debts
        for (Iou iou : lender.owes()) {
            lenderOwes.put(iou.name, iou.amount);
        }
        
        for (Iou iou : lender.owedBy()) {
            lenderOwedBy.put(iou.name, iou.amount);
        }
        
        for (Iou iou : borrower.owes()) {
            borrowerOwes.put(iou.name, iou.amount);
        }
        
        for (Iou iou : borrower.owedBy()) {
            borrowerOwedBy.put(iou.name, iou.amount);
        }
        
        // Process the IOU
        String lenderName = lender.name();
        String borrowerName = borrower.name();
        
        // Check if borrower already owes lender
        double borrowerOwesToLender = borrowerOwes.getOrDefault(lenderName, 0.0);
        
        // Check if lender already owes borrower
        double lenderOwesToBorrower = lenderOwes.getOrDefault(borrowerName, 0.0);
        
        if (lenderOwesToBorrower > 0) {
            // Lender already owes borrower, so reduce that debt first
            if (amount <= lenderOwesToBorrower) {
                // Just reduce the existing debt
                double newDebt = lenderOwesToBorrower - amount;
                if (newDebt > 0) {
                    lenderOwes.put(borrowerName, newDebt);
                    borrowerOwedBy.put(lenderName, newDebt);
                } else {
                    lenderOwes.remove(borrowerName);
                    borrowerOwedBy.remove(lenderName);
                }
            } else {
                // Clear the existing debt and create a new debt in the opposite direction
                lenderOwes.remove(borrowerName);
                borrowerOwedBy.remove(lenderName);
                
                double newDebt = amount - lenderOwesToBorrower;
                borrowerOwes.put(lenderName, newDebt);
                lenderOwedBy.put(borrowerName, newDebt);
            }
        } else if (borrowerOwesToLender > 0) {
            // Borrower already owes lender, so just add to that debt
            double newDebt = borrowerOwesToLender + amount;
            borrowerOwes.put(lenderName, newDebt);
            lenderOwedBy.put(borrowerName, newDebt);
        } else {
            // No existing debt, create a new one
            borrowerOwes.put(lenderName, amount);
            lenderOwedBy.put(borrowerName, amount);
        }
        
        // Build updated users
        User.Builder lenderBuilder = User.builder().setName(lenderName);
        User.Builder borrowerBuilder = User.builder().setName(borrowerName);
        
        for (Map.Entry<String, Double> entry : lenderOwes.entrySet()) {
            lenderBuilder.owes(entry.getKey(), entry.getValue());
        }
        
        for (Map.Entry<String, Double> entry : lenderOwedBy.entrySet()) {
            lenderBuilder.owedBy(entry.getKey(), entry.getValue());
        }
        
        for (Map.Entry<String, Double> entry : borrowerOwes.entrySet()) {
            borrowerBuilder.owes(entry.getKey(), entry.getValue());
        }
        
        for (Map.Entry<String, Double> entry : borrowerOwedBy.entrySet()) {
            borrowerBuilder.owedBy(entry.getKey(), entry.getValue());
        }
        
        return Arrays.asList(lenderBuilder.build(), borrowerBuilder.build());
    }

    private JSONObject convertUserToJson(User user) {
        JSONObject userJson = new JSONObject();
        userJson.put("name", user.name());
        
        JSONObject owesJson = new JSONObject();
        JSONObject owedByJson = new JSONObject();
        
        for (Iou iou : user.owes()) {
            owesJson.put(iou.name, iou.amount);
        }
        
        for (Iou iou : user.owedBy()) {
            owedByJson.put(iou.name, iou.amount);
        }
        
        userJson.put("owes", owesJson);
        userJson.put("owedBy", owedByJson);
        
        // Calculate balance
        double totalOwedBy = user.owedBy().stream().mapToDouble(iou -> iou.amount).sum();
        double totalOwes = user.owes().stream().mapToDouble(iou -> iou.amount).sum();
        double balance = totalOwedBy - totalOwes;
        
        userJson.put("balance", balance);
        
        return userJson;
    }
}