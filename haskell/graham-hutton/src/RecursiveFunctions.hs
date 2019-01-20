module RecursiveFunctions where

factorial :: Int -> Int
factorial 0 = 1
factorial n = n * factorial (n - 1)

mul :: Int -> Int -> Int
mul m 0 = 0
mul m n = m + (mul m (n - 1))

myProduct :: Num a => [a] -> a
myProduct []     = 1
myProduct (x:xs) = x * myProduct xs

myReverse :: [a] -> [a]
myReverse []     = []
myReverse (x:xs) = (myReverse xs) ++ [x]

insert :: Ord a => a -> [a] -> [a]
insert x [] = [x]
insert x (y:ys)
  | x <= y = x : y : ys
  | otherwise = y : insert x ys

isort :: Ord a => [a] -> [a]
isort []     = []
isort (x:xs) = insert x (isort xs)

myZip :: [a] -> [b] -> [(a, b)]
myZip [] _          = []
myZip _ []          = []
myZip (x:xs) (y:ys) = (x, y) : myZip xs ys

myDrop :: Int -> [a] -> [a]
myDrop 0 xs     = xs
myDrop n []     = []
myDrop n (x:xs) = drop (n - 1) xs

fib :: Int -> Int
fib 0 = 0
fib 1 = 1
fib n = fib a + fib b
  where
    a = n - 2
    b = n - 1

myEven :: Int -> Bool
myEven 0 = True
myEven n = myOdd (n - 1)

myOdd :: Int -> Bool
myOdd 0 = False
myOdd n = myEven (n - 1)

-- Exercises
-- b^n
expo :: Int -> Int -> Int
expo 1 _ = 1
expo b 1 = b
expo b n = b * expo b (n - 1)

and' :: [Bool] -> Bool
and' []     = True
and' (x:xs) = x && and' xs

concat' :: [[a]] -> [a]
concat' []       = []
concat' (xs:xss) = xs ++ concat' xss

replicate' :: Int -> a -> [a]
replicate' 0 _ = []
replicate' n x = x : replicate' (n - 1) x

index' :: [a] -> Int -> a
index' [] _     = error "index out of bounds"
index' (x:_) 0  = x
index' (_:xs) n = index' xs (n - 1)

elem' :: Eq a => a -> [a] -> Bool
elem' x [] = False
elem' x (y:ys)
  | x == y = True
  | otherwise = elem' x ys

merge :: Ord a => [a] -> [a] -> [a]
merge [] ys = ys
merge xs [] = xs
merge (x:xs) (y:ys)
  | x <= y = x : merge xs (y : ys)
  | otherwise = y : merge (x : xs) ys

halve :: [a] -> ([a], [a])
halve xs = ((take n xs), (drop n xs))
  where
    n = length xs `div` 2

msort :: Ord a => [a] -> [a]
msort [] = []
msort [x] = [x]
msort xs = merge (msort first) (msort second)
  where
    (first, second) = halve xs

sum' :: Num a => [a] -> a
sum' []     = 0
sum' (x:xs) = x + sum' xs

take' :: Int -> [a] -> [a]
take' n []     = []
take' 0 _      = []
take' n (x:xs) = x : take' (n - 1) xs

last' :: [a] -> a
last' []     = error "empty list"
last' (x:[]) = x
last' (x:xs) = last' xs
