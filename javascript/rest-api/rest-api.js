//
// This is only a SKELETON file for the 'Rest API' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class RestAPI {
  constructor(database = { users: [] }) {
    this.users = [...database.users];
  }

  get(url) {
    if (url === '/users') {
      return { users: [...this.users] };
    }

    // Handle /users?users=X,Y,Z
    const match = url.match(/\/users\?users=(.+)/);
    if (match) {
      const requestedUsers = match[1].split(',');
      const filteredUsers = this.users.filter(user => 
        requestedUsers.includes(user.name)
      );
      return { users: filteredUsers };
    }

    throw new Error(`Unsupported GET endpoint: ${url}`);
  }

  post(url, payload) {
    if (url === '/add') {
      const newUser = {
        name: payload.user,
        owes: {},
        owed_by: {},
        balance: 0
      };
      this.users.push(newUser);
      return newUser;
    }

    if (url === '/iou') {
      const { lender, borrower, amount } = payload;
      
      // Find the users
      const lenderUser = this.users.find(user => user.name === lender);
      const borrowerUser = this.users.find(user => user.name === borrower);
      
      if (!lenderUser || !borrowerUser) {
        throw new Error('User not found');
      }

      // Handle the case where the borrower already owes the lender
      if (borrowerUser.owes[lender]) {
        borrowerUser.owes[lender] += amount;
      } 
      // Handle the case where the lender already owes the borrower
      else if (lenderUser.owes[borrower]) {
        if (lenderUser.owes[borrower] > amount) {
          lenderUser.owes[borrower] -= amount;
          borrowerUser.owed_by[lender] -= amount;
        } else {
          const diff = amount - lenderUser.owes[borrower];
          delete lenderUser.owes[borrower];
          delete borrowerUser.owed_by[lender];
          
          if (diff > 0) {
            borrowerUser.owes[lender] = diff;
            lenderUser.owed_by[borrower] = diff;
          }
        }
      }
      // Handle the standard case
      else {
        if (!borrowerUser.owes[lender]) {
          borrowerUser.owes[lender] = 0;
        }
        if (!lenderUser.owed_by[borrower]) {
          lenderUser.owed_by[borrower] = 0;
        }
        
        borrowerUser.owes[lender] += amount;
        lenderUser.owed_by[borrower] += amount;
      }

      // Clean up zero values
      if (borrowerUser.owes[lender] === 0) {
        delete borrowerUser.owes[lender];
      }
      if (lenderUser.owed_by[borrower] === 0) {
        delete lenderUser.owed_by[borrower];
      }

      // Update balances
      this.updateBalance(lenderUser);
      this.updateBalance(borrowerUser);

      // Return the updated users, sorted by name
      const updatedUsers = [lenderUser, borrowerUser].sort((a, b) => 
        a.name.localeCompare(b.name)
      );
      
      return { users: updatedUsers };
    }

    throw new Error(`Unsupported POST endpoint: ${url}`);
  }

  updateBalance(user) {
    // Calculate total owed by others
    const totalOwedBy = Object.values(user.owed_by).reduce(
      (sum, amount) => sum + amount, 
      0
    );
    
    // Calculate total owed to others
    const totalOwes = Object.values(user.owes).reduce(
      (sum, amount) => sum + amount, 
      0
    );
    
    // Update balance
    user.balance = totalOwedBy - totalOwes;
  }
}
