'use strict';
import bcrypt from "bcryptjs";

export function hash (password, {salt = null}={}) {
  return bcrypt.hashSync(password, salt === null ? 12 : salt);
}