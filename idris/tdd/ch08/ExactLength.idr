module ExactLength

data Vect : (n : Nat) -> (t: Type) -> Type where
  Nil : Vect Z t
  (::) : (a : t) -> (v : Vect k t) -> Vect (S k) t

exactLength : (len : Nat) -> (v : Vect n t) -> Maybe (Vect len t)
exactLength {n} len v = case len == n of
                             False => Nothing
                             True => ?exactLength_rhs_2 -- `Just v` won't work.
