import bcrypt from "bcryptjs";

export function hash (password) {
  let salt = bcrypt.genSaltSync(12);
  return bcrypt.hashSync(password, salt);
}