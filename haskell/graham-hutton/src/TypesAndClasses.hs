module TypesAndClasses where

add :: (Int, Int) -> Int
add (a, b) = a + b

add' :: Int -> (Int -> Int)
add' x y = x + y

mult :: Int -> (Int -> (Int -> Int))
mult x y z = z * y * z

inc :: Int -> Int
inc = add' 1

{-
type notation `->` is right associative
functon application is left associative
-}

{-
['a', 'b', 'c'] :: [Char]
('a', 'b', 'c') :: (Char, Char, Char)
[(False, '0'), (True, '1')] :: [(Bool, Char)]
([False, True], ['0', '1']) :: ([Bool], [Char])
[tail, init, reverse] :: [[a] -> [a]]
-}

second :: [a] -> a
second xs = head (tail xs)

swap :: (a, a) -> (a, a)
swap (x, y) = (y, x)

pair :: a -> b -> (a, b)
pair x y = (x, y)

double :: Num a => a -> a
double x = x * 2

palindrome :: Eq a => [a] -> Bool
palindrome xs = reverse xs == xs

twice :: (a -> a) -> a -> a
twice f x = f (f x)
