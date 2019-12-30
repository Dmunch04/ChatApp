import bcrypt from "bcryptjs";

export function hash (password) {
  return bcrypt.hashSync(password, 12);
}