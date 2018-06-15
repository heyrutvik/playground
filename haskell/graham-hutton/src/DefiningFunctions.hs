module DefiningFunctions where

isDigit :: Char -> Bool
isDigit c = c >= '0' && c <= '9'

isEven :: Integral a => a -> Bool
isEven n = n `mod` 2 == 0

mySplitAt :: Int -> [a] -> ([a], [a])
mySplitAt n xs = (take n xs, drop n xs)

myRecip :: Fractional a => a -> a
myRecip n = 1 / n

myAbs :: Int -> Int
myAbs n = if n >= 0 then n else -n

myAbs' :: Int -> Int
myAbs' n | n >= 0 = n
         | otherwise = -n

signum' :: Int -> Int
signum' n | n > 0 = 1
          | n == 0 = 0
          | otherwise = -1

not' :: Bool -> Bool
not' False = True
not' True = False

and' :: Bool -> Bool -> Bool
True `and'` b = b
False `and'` _ = False

and'' :: Bool -> Bool -> Bool
and'' a b = if a == b && b == True then True else False

and''' :: Bool -> Bool -> Bool
and''' a b = if a == True then b else
               if a == False then a else b

fst' :: (a, b) -> a
fst' (x, _) = x

snd' :: (a, b) -> b
snd' (_, y) = y

halve :: [a] -> ([a], [a])
halve xs = (take h xs, drop h xs)
           where
             h = length xs `div` 2

safetail' :: [a] -> [a]
safetail' xs = if null xs then [] else tail xs

safetail'' :: [a] -> [a]
safetail'' xs | null xs = []
             | otherwise = tail xs

safetail''' :: [a] -> [a]
safetail''' [] = []
safetail''' (x : xs) = xs

or' :: Bool -> Bool -> Bool
True `or'` _ = True
False `or'` b = b

or'' :: Bool -> Bool -> Bool
or'' a b | a == b && a == False = False
         | otherwise = True

mult :: Num a => a -> a -> a -> a
mult x y z = x * y * z

mult' :: Num a => a -> (a -> (a -> a))
mult' = \x -> (\y -> (\z -> x * y * z))

myAnd :: [Bool] -> Bool
myAnd = foldr (&&) True
