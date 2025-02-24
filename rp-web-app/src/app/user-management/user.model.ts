export class User {
  id!: number;            // Unique identifier for the user
  username!: string;      // Username of the user
  email!: string;         // Email of the user
  name!: string;          // Full name of the user
  roles!: string[];          // Role of the user (e.g., ADMIN, CASHER, OPERATOR etc.)
  enabled!: boolean;      // Status of the user. User may be disabled by the admin or too many entry of wrong password
}

export const UserRoles = [
  { label: 'Admin', value: 'ADMIN' },
  { label: 'Manager', value: 'MANAGER' },
  { label: 'Cashier', value: 'CASHIER' },
];