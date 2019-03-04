module Id

-- mimic the `=` type
data Id : a -> b -> Type where
  Refl : Id x x

checkEqNat : (num1 : Nat) -> (num2 : Nat) -> Maybe (num1 = num2)
checkEqNat Z Z = Just Refl
checkEqNat (S k) Z = Nothing
checkEqNat Z (S j) = Nothing
checkEqNat (S k) (S j) = case checkEqNat k j of
                              Nothing => Nothing
                              (Just prf) => Just (cong prf)
