export class User {
  id!: BigInt;            // Unique identifier for the user
  username!: string;      // Username of the user
  email!: string;         // Email of the user
  name!: string;          // Full name of the user
  role!: string;          // Role of the user (e.g., ADMIN, CASHER, OPERATOR etc.)
  enabled!: boolean;      // Status of the user. User may be disabled by the admin or too many entry of wrong password
}