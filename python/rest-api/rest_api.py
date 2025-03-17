import json

class RestAPI:
    def __init__(self, database=None):
        self.database = database or {"users": []}

    def get(self, url, payload=None):
        if url == "/users":
            if payload is None:
                return json.dumps(self.database)
            
            payload_data = json.loads(payload)
            requested_users = payload_data.get("users", [])
            
            filtered_users = [user for user in self.database["users"] 
                             if user["name"] in requested_users]
            
            return json.dumps({"users": filtered_users})
        
        return json.dumps({"error": "Invalid URL"})

    def post(self, url, payload=None):
        payload_data = json.loads(payload) if payload else {}
        
        if url == "/add":
            new_user = {
                "name": payload_data["user"],
                "owes": {},
                "owed_by": {},
                "balance": 0.0
            }
            
            self.database["users"].append(new_user)
            return json.dumps(new_user)
        
        elif url == "/iou":
            lender_name = payload_data["lender"]
            borrower_name = payload_data["borrower"]
            amount = float(payload_data["amount"])
            
            # Find the users
            lender = next((user for user in self.database["users"] if user["name"] == lender_name), None)
            borrower = next((user for user in self.database["users"] if user["name"] == borrower_name), None)
            
            # Handle existing debts between users
            existing_debt = lender["owes"].get(borrower_name, 0)
            
            if existing_debt > 0:
                # Lender already owes borrower
                if amount <= existing_debt:
                    # Reduce existing debt
                    lender["owes"][borrower_name] -= amount
                    borrower["owed_by"][lender_name] -= amount
                    
                    # Remove zero debts
                    if lender["owes"][borrower_name] == 0:
                        del lender["owes"][borrower_name]
                        del borrower["owed_by"][lender_name]
                else:
                    # Debt direction changes
                    del lender["owes"][borrower_name]
                    del borrower["owed_by"][lender_name]
                    
                    # Borrower now owes lender
                    net_amount = amount - existing_debt
                    borrower["owes"][lender_name] = net_amount
                    lender["owed_by"][borrower_name] = net_amount
            else:
                # Borrower already owes lender or no existing debt
                existing_credit = lender["owed_by"].get(borrower_name, 0)
                borrower_owes = borrower["owes"].get(lender_name, 0)
                
                # Update or create the debt
                new_debt = existing_credit + amount
                borrower["owes"][lender_name] = new_debt
                lender["owed_by"][borrower_name] = new_debt
            
            # Update balances
            self._update_balance(lender)
            self._update_balance(borrower)
            
            # Return the updated users (sorted by name)
            updated_users = [lender, borrower]
            updated_users.sort(key=lambda x: x["name"])
            
            return json.dumps({"users": updated_users})
        
        return json.dumps({"error": "Invalid URL"})
    
    def _update_balance(self, user):
        """Update the balance for a user based on owes and owed_by"""
        total_owed_by = sum(user["owed_by"].values())
        total_owes = sum(user["owes"].values())
        user["balance"] = total_owed_by - total_owes
