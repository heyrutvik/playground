module Vectors

import Data.Vect

fourVec : Vect 4 Int
fourVec = [1,2,3,4]

threeVec : Vect 3 Int
threeVec = [5,6,7]

sevenVec : Vect 7 Int
sevenVec = fourVec ++ threeVec

allLengths : Vect k String -> Vect k Nat
allLengths [] = []
allLengths (word :: words) = length word :: allLengths words
