module DeclaringTypes where

-- type declarations
type Bit = Int

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

add' :: Nat -> Nat -> Nat
add' m n = int2nat (nat2int m + nat2int n)

add :: Nat -> Nat -> Nat
add Zero n     = n
add (Succ m) n = Succ (add m n)

{-
mult 3 2
add 2 (mult 2 2)
add 2 (add 2 (mult 1 2))
add 2 (add 2 (2))
add 2 4
6
-}
mult :: Nat -> Nat -> Nat
mult Zero n        = Zero
mult (Succ Zero) n = n
mult (Succ m) n    = add (n) (mult m n)

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

-- tautology checker
data Prop
  = Const Bool
  | Var Char
  | Not Prop
  | And Prop
        Prop
  | Imply Prop
          Prop

-- A /\ ~A
p1 :: Prop
p1 = And (Var 'A') (Not (Var 'A'))

-- (A /\ B) => A
p2 :: Prop
p2 = Imply (And (Var 'A') (Var 'B')) (Var 'A')

-- A => (A /\ B)
p3 :: Prop
p3 = Imply (Var 'A') (And (Var 'A') (Var 'B'))

-- (A /\ (A => B)) => B
p4 :: Prop
p4 = Imply (And (Var 'A') (Imply (Var 'A') (Var 'B'))) (Var 'B')

type Subst = Assoc Char Bool

eval :: Subst -> Prop -> Bool
eval _ (Const b)   = b
eval s (Var x)     = find x s
eval s (Not p)     = not (eval s p)
eval s (And p q)   = eval s p && eval s q
eval s (Imply p q) = eval s p <= eval s q

vars :: Prop -> [Char]
vars (Const _)   = []
vars (Var x)     = [x]
vars (Not p)     = vars p
vars (And p q)   = vars p ++ vars q
vars (Imply p q) = vars p ++ vars q

bools :: Int -> [[Bool]]
bools n = map f [0 .. limit]
  where
    limit = (2 ^ n) - 1 -- max list we need
    make n bs = take n (bs ++ repeat 0) -- append 0 till we get n bit-length
    conv 0 = False -- convert 0 to False
    conv 1 = True -- convert 1 to True
    f = map conv . make n . int2bin -- integer to binary then adject the length then convert each 0/1 to boolean

int2bin :: Int -> [Bit]
int2bin 0 = []
int2bin n = n `mod` 2 : int2bin (n `div` 2)

bools' :: Int -> [[Bool]]
bools' 0 = [[]]
bools' n = map (False :) bss ++ map (True :) bss
  where
    bss = bools' (n - 1)

rmdups :: Eq a => [a] -> [a]
rmdups []     = []
rmdups (x:xs) = x : rmdups (filter (/= x) xs)

substs :: Prop -> [Subst]
substs p = map (zip vs) (bools (length vs))
  where
    vs = rmdups (vars p)

isTaut :: Prop -> Bool
isTaut p = and [eval s p | s <- substs p]

--
data Ordering'
  = LT'
  | EQ'
  | GT'
  deriving (Show, Eq)

compare' :: Ord a => a -> a -> Ordering'
compare' m n
  | m < n = LT'
  | m > n = GT'
  | otherwise = EQ'

occurs' :: Int -> Tree -> Bool
occurs' m (Leaf n) = (compare' m n) == EQ'
occurs' m (Node l n r) =
  case compare' m n of
    EQ' -> True
    LT' -> occurs' m l
    GT' -> occurs' m r

data BTree
  = BLeaf Int
  | BNode BTree
          BTree

countLeaf :: BTree -> Int
countLeaf (BLeaf n)   = 1
countLeaf (BNode l r) = countLeaf l + countLeaf r

t1 :: BTree
t1 = BNode (BNode (BLeaf 1) (BLeaf 2)) (BLeaf 3)

balanced :: BTree -> Bool
balanced (BLeaf n)   = True
balanced (BNode l r) = countLeaf l == countLeaf r && balanced l && balanced r
