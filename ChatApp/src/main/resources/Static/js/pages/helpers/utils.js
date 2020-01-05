export function checkIfError(possibleError) {
  return possibleError.hasOwnProperty("Code") && possibleError.hasOwnProperty("Type") && possibleError.hasOwnProperty("Error")
}