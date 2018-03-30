import Data.Vect

insert : (x : elem) -> (xsSorted : Vect len elem) -> Vect (S len) elem
insert x [] = [x]
insert x (y :: xs) = ?insert_rhs_2

insSort : Vect n elem -> Vect n elem
insSort [] = []
insSort (x :: xs) = let xsSorted = insSort xs in
                        insert x xsSorted
