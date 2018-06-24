module DeclaringTypes where

-- type declarations
type String = [Char]

type Board = [Pos]

type Pos = (Int, Int)

-- type Tree = (Int, [Tree]) // use `data` for recusrive type
type Assoc k v = [(k, v)]

find :: Eq k => k -> Assoc k v -> v
find k t = head [v | (k', v) <- t, k == k']

-- data declarations
data Animal
  = Dog
  | Cat

printAnimal :: Animal -> IO ()
printAnimal Dog = print "dog"
printAnimal Cat = print "cat"

-- constructor with arguments
data Shape
  = Circle Float
  | Rect Float
         Float

square :: Float -> Shape
square n = Rect n n

data Option a
  = None
  | Some a

safediv :: Int -> Int -> Option Int
safediv _ 0 = None
safediv m n = Some (m `div` n)

-- recursive types
data Nat
  = Zero
  | Succ Nat

nat2int :: Nat -> Int
nat2int Zero     = 0
nat2int (Succ n) = 1 + nat2int n

int2nat :: Int -> Nat
int2nat 0 = Zero
int2nat n = Succ (int2nat (n - 1))

add :: Nat -> Nat -> Nat
add m n = int2nat (nat2int m + nat2int n)

add' :: Nat -> Nat -> Nat
add' Zero n     = n
add' (Succ m) n = Succ (add' m n)

data List a
  = Nil
  | Cons a
         (List a)

len :: List a -> Int
len Nil         = 0
len (Cons _ xs) = 1 + len xs

data Tree
  = Leaf Int
  | Node Tree
         Int
         Tree

t :: Tree
t = Node (Node (Leaf 1) 3 (Leaf 4)) 5 (Node (Leaf 6) 7 (Leaf 9))

occurs :: Int -> Tree -> Bool
occurs m (Leaf n)     = m == n
occurs m (Node l n r) = m == n || occurs m l || occurs m r

flatten :: Tree -> [Int]
flatten (Leaf n)     = [n]
flatten (Node l n r) = flatten l ++ [n] ++ flatten r
