module Exercises

import Data.Vect

data Tree : Type -> Type where
  Empty : Ord t => Tree t
  Node : Ord t => (left : Tree t) -> (val : t) -> (right : Tree t) -> Tree t

%name Tree tree, tree1, tree2

insert : t -> Tree t -> Tree t
insert x Empty = Node Empty x Empty
insert x node@(Node left y right) = case compare x y of
                                    LT => Node (insert x left) y right
                                    EQ => node
                                    GT => Node left y (insert x right)

listToTree : Ord a => List a -> Tree a
listToTree xs = listToTreeHelper xs Empty where
  listToTreeHelper : Ord a => List a -> Tree a -> Tree a
  listToTreeHelper [] acc = acc
  listToTreeHelper (x :: xs) acc = listToTreeHelper xs (insert x acc)

treeToList : Tree a -> List a
treeToList Empty = []
treeToList (Node left val right) = treeToList left ++ [val] ++ treeToList right

data Expr : Type where
  Lit : Int -> Expr
  Add : Expr -> Expr -> Expr
  Sub : Expr -> Expr -> Expr
  Mul : Expr -> Expr -> Expr

evaluate : Expr -> Int
evaluate (Lit x) = x
evaluate (Add x y) = (evaluate x) + (evaluate y)
evaluate (Sub x y) = (evaluate x) - (evaluate y)
evaluate (Mul x y) = (evaluate x) * (evaluate y)

maxMaybe : Ord a => Maybe a -> Maybe a -> Maybe a
maxMaybe Nothing Nothing = Nothing
maxMaybe Nothing y = y
maxMaybe x Nothing = x
maxMaybe a@(Just x) b@(Just y) = case compare x y of
                                  LT => b
                                  EQ => a
                                  GT => a

data Shape = Triangle Double Double
           | Rectangle Double Double
           | Circle Double

data Picture = Primitive Shape
             | Combine Picture Picture
             | Rotate Double Picture
             | Translate Double Double Picture

biggestTriangle : Picture -> Maybe Double
biggestTriangle (Primitive (Triangle x y)) = (Just (0.5 * x * y))
biggestTriangle (Primitive s) = Nothing
biggestTriangle (Combine x y) = maxMaybe (biggestTriangle x) (biggestTriangle y)
biggestTriangle (Rotate x y) = maxMaybe (biggestTriangle y) Nothing
biggestTriangle (Translate x y z) = maxMaybe (biggestTriangle z) Nothing

takeVec : (k : Nat) -> Vect (k+n) t -> Vect k t
takeVec Z xs = []
takeVec (S k) (x :: xs) = x :: (takeVec k xs)

sumEntries : Num a => (pos : Integer) -> Vect n a -> Vect n a -> Maybe a
sumEntries {n} pos xs ys = case integerToFin pos n of
                            Nothing => Nothing
                            (Just x) => Just ((Vect.index x xs) + (Vect.index x ys))
