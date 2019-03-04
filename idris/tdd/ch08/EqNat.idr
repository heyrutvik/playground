module EqNat

import Data.Vect

data EqNat : (num1 : Nat) -> (num2 : Nat) -> Type where
  Same : (num : Nat) -> EqNat num num

-- if k and j are equal, S k and S j are also equal.
sameS : (eq : EqNat k j) -> EqNat (S k) (S j)
sameS (Same k) = Same (S k)

-- if k and j are equal, f k and f j are also equal.
sameF : (f : Nat -> Nat) -> (eq : EqNat k j) -> EqNat (f k) (f j)
sameF f (Same k) = Same (f k)

checkEqNat : (num1 : Nat) -> (num2 : Nat) -> Maybe (EqNat num1 num2)
checkEqNat Z Z = Just (Same 0)
checkEqNat (S k) Z = Nothing
checkEqNat Z (S j) = Nothing
checkEqNat (S k) (S j) = case checkEqNat k j of
                              Nothing => Nothing
                              (Just eq) => Just (sameS eq)

exactLength : (len : Nat) -> (v : Vect n t) -> Maybe (Vect len t)
exactLength {n} len v = case checkEqNat n len of
                             Nothing => Nothing
                             Just (Same len) => Just v -- try without pattern matching `eq`
