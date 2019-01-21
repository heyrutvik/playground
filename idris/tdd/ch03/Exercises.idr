module Exercises

import Data.Vect

total my_length : List a -> Nat
my_length [] = 0
my_length (x :: xs) = 1 + (my_length xs)

total my_reverse : List a -> List a
my_reverse [] = []
my_reverse (x :: xs) = my_reverse xs ++ [x]

total my_list_map : (a -> b) -> List a -> List b
my_list_map f [] = []
my_list_map f (x :: xs) = f x :: my_list_map f xs

total my_vect_map : (a -> b) -> Vect n a -> Vect n b
my_vect_map f [] = []
my_vect_map f (x :: xs) = f x :: my_vect_map f xs

-- https://gist.github.com/heyrutvik/d88e171e4526a8a143c0df61a24d9461
