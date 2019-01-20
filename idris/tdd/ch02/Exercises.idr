module Exercises

palindrome : String -> Bool
palindrome str = str == (reverse str)

palindrome' : String -> Bool
palindrome' str = let str' = toLower str in palindrome str'

palindrome'' : String -> Bool
palindrome'' str = if length str > 10 then palindrome' str else False

palindrome''' : Nat -> String -> Bool
palindrome''' k str = if length str > k then palindrome' str else False

count : String -> (Nat, Nat)
count str = (length (words str), (length str))

top_ten : Ord a => List a -> List a
top_ten = (List.take 10) . reverse . sort

over_length : Nat -> List String -> Nat
over_length max strs = List.length (filter (\str => (Strings.length str) > max) strs)

replPalindrome : IO ()
replPalindrome = repl "Enter a string: " palindromeHelper
  where
    palindromeHelper : String -> String
    palindromeHelper str = case palindrome' str of
                                True => str ++ " is palindrome! :)\n"
                                False => str ++ " is not palindrome! :(\n"

replCount : IO ()
replCount = repl "Enter a string: " countHelper
  where
    countHelper : String -> String
    countHelper str = case count str of
                           (wordLen, totalChar) => str ++ " has " ++ (cast wordLen) ++
                                                   " words and " ++ (cast totalChar) ++
                                                   " characters.\n"
