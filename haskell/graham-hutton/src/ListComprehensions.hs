module ListComprehensions where

import           Data.Char

myConcat :: [[a]] -> [a]
myConcat xss = [x | xs <- xss, x <- xs]

firsts :: [(a, b)] -> [a]
firsts ps = [x | (x, _) <- ps]

myLength :: [a] -> Int
myLength xs = sum [1 | _ <- xs]

factors :: Int -> [Int]
factors n = [x | x <- [1 .. n], n `mod` x == 0]

prime :: Int -> Bool
prime n = factors n == [1, n]

primes :: Int -> [Int]
primes n = [x | x <- [2 .. n], prime x]

find :: Eq a => a -> [(a, b)] -> [b]
find k ps = [b | (a, b) <- ps, a == k]

pairs :: [a] -> [(a, a)]
pairs xs = zip xs (tail xs)

sorted :: Ord a => [a] -> Bool
sorted xs = and [x <= y | (x, y) <- pairs xs]

positions :: Eq a => a -> [a] -> [Int]
positions k xs = [i | (x, i) <- zip xs [0 .. n], x == k]
  where
    n = length xs - 1

lowers :: String -> Int
lowers xs = length [x | x <- xs, isLower x]

count :: Char -> String -> Int
count x xs = length [y | y <- xs, x == y]

-- The Caesar cipher
let2int :: Char -> Int
let2int c = ord c - ord 'a'

int2let :: Int -> Char
int2let n = chr (ord 'a' + n)

shift :: Int -> Char -> Char
shift n c
  | isLower c = int2let ((let2int c + n) `mod` 26)
  | otherwise = c

encode :: Int -> String -> String
encode n xs = [shift n x | x <- xs]

decode :: Int -> String -> String
decode n xs = encode (-n) xs

table :: [Float]
table =
  [ 8.2
  , 1.5
  , 2.8
  , 4.3
  , 12.7
  , 2.2
  , 2.0
  , 6.1
  , 7.0
  , 0.2
  , 0.8
  , 4.0
  , 2.4
  , 6.7
  , 7.5
  , 1.9
  , 0.1
  , 6.0
  , 6.3
  , 9.1
  , 2.8
  , 1.0
  , 2.4
  , 0.2
  , 2.0
  , 0.1
  ]

percent :: Int -> Int -> Float
percent n m = (fromIntegral n / fromIntegral m) * 100

freqs :: String -> [Float]
freqs cs = [percent (count c cs) n | c <- ['a' .. 'z']]
  where
    n = lowers cs

-- Exercises
sumOfSquare :: Int -> Int
sumOfSquare n = sum [x ^ 2 | x <- [1 .. n]]

myReplicate :: Int -> a -> [a]
myReplicate n x = [x | _ <- [1 .. n]]

pyths :: Int -> [(Int, Int, Int)]
pyths n =
  [ (x, y, z)
  | x <- [1 .. n]
  , y <- [1 .. n]
  , z <- [1 .. n]
  , ((x ^ 2) + (y ^ 2)) == (z ^ 2)
  ]

perfects :: Int -> [Int]
perfects n = [x | x <- [1 .. n], x == sum (init (factors x))]

singleComprehension :: [(Int, Int)]
singleComprehension = [(x, y) | x <- [1, 2], y <- [3, 4]]

doubleComprehension :: [(Int, Int)]
doubleComprehension = concat [[(x, y) | y <- [3, 4]] | x <- [1, 2]]

positions' :: Eq a => a -> [a] -> [Int]
positions' k xs = find k [(x, i) | (x, i) <- zip xs [0 .. n]]
  where
    n = length xs - 1

scalarproduct :: Num a => [a] -> [a] -> a
scalarproduct xs ys = sum [x * y | (x, y) <- zip xs ys]
