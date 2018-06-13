module Introduction where

double x = x + x

mySum [] = 0
mySum (x : xs) = x + mySum xs

-- by replacing `<=` with `<` removes duplicate numbers from the list
myQsort [] = []
myQsort (x : xs) = myQsort smaller ++ [x] ++ myQsort larger
                   where
                     smaller = [a | a <- xs, a <= x]
                     larger = [b | b <- xs, b > x]

myProduct [] = 1
myProduct (x : xs) = x * myProduct xs

myReverseQsort [] = []
myReverseQsort (x : xs) = myReverseQsort larger ++ [x] ++ myReverseQsort smaller
                          where
                            larger = [a | a <- xs, a > x]
                            smaller = [b | b <- xs, b <= x]
