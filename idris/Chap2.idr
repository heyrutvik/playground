module Chap2

palindrome : Nat -> String -> Bool
palindrome n s = let lowercase = toLower s in
               if ((length s) > n) then lowercase == (reverse lowercase) else False

counts : String -> (Nat, Nat)
counts s = (length (words s), length s)

top_ten : Ord a => List a -> List a
top_ten xs = take 10 (reverse (sort xs))

over_length : (n : Nat) -> (xs : List String) -> Nat
over_length n xs = size (filter (\x => (length x) > n) xs)

main : IO ()
main = repl "Enter a string: " (\x => if ((palindrome 0 x) == True) then "True\n" else "False\n")
