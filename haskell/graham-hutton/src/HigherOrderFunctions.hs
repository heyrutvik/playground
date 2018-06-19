module HigherOrderFunctions where

import Data.Char

length' :: [a] -> Int
length' = foldr (\_ -> \v -> 1 + v) 0

reverse' :: [a] -> [a]
reverse' = foldr (\x -> \v -> v ++ [x]) []

reverse'' :: [a] -> [a]
reverse'' = foldl (\v -> \x -> x : v) []

compose :: [a -> a] -> (a -> a)
compose = foldr (.) id

type Bit = Int

bin2int :: [Bit] -> Int
bin2int bits = sum [w * b | (w, b) <- zip weights bits]
               where
                 weights = iterate (*2) 1

bin2int' :: [Bit] -> Int
bin2int' = foldr (\x y -> x + 2 * y) 0

int2bin :: Int -> [Bit]
int2bin 0 = []
int2bin n = n `mod` 2 : int2bin (n `div` 2)

make8 :: [Bit] -> [Bit]
make8 bits = take 8 (bits ++ repeat 0)

encode :: String -> [Bit]
encode = concat . map (make8 . int2bin . ord)

chop8 :: [Bit] -> [[Bit]]
chop8 [] = []
chop8 bits = take 8 bits : chop8 (drop 8 bits)

decode :: [Bit] -> String
decode = map (chr . bin2int) . chop8

transmit :: String -> String
transmit = decode . channel . encode

channel :: [Bit] -> [Bit]
channel = id

listComprehension :: (a -> Bool) -> (a -> a) -> [a] -> [a]
listComprehension p f xs = [f x | x <- xs, p x]

listComprehension' :: (a -> Bool) -> (a -> a) -> [a] -> [a]
listComprehension' p f = (map f) . (filter p)

myAll :: (a -> Bool) -> [a] -> Bool
myAll f = (foldr (&&) True) . (map f)

myAny :: (a -> Bool) -> [a] -> Bool
myAny f = (foldr (||) False) . (map f)

myTakeWhile :: (a -> Bool) -> [a] -> [a]
myTakeWhile _ [] = []
myTakeWhile p (x : xs) | p x = x : myTakeWhile p xs
                       | otherwise = []

myDropWhile :: (a -> Bool) -> [a] -> [a]
myDropWhile _ [] = []
myDropWhile p (x : xs) | p x = myDropWhile p xs
                       | otherwise = xs

myMap :: (a -> b) -> [a] -> [b]
myMap f = foldr (\x y -> f x : y) []

myFilter :: (a -> Bool) -> [a] -> [a]
myFilter p = foldr (\x y -> if p x then x : y else y) []

dec2int :: [Int] -> Int
dec2int = base2int 10

bin2int'' :: [Bit] -> Int
bin2int'' = base2int 2

base2int :: Int -> [Int] -> Int
base2int b xs = sum [y * w | (y, w) <- zip ys weights]
             where
               weights = (map (b^) [0..])
               ys = reverse xs

inc = (+1)
double = (*2)
doubleThenInc = inc . double
incThenDouble = double . inc
incThenDouble' = compose [inc, double]
doubleThenInc' = compose [double, inc]
-- incThenDouble 1 == doubleThenInc' 1

-- won't compile becuase compose take list of function which has same type
-- here `map (^2)` and `filter even` has same type [a] -> [a]
-- but sum has type [a] -> a
-- sumsqreven = compose [sum, map (^2), filter even]

plus :: Num a => (a, a) -> a
plus (x, y) = x + y

my2curry :: ((a, b) -> c) -> a -> b -> c
my2curry f = \x -> \y -> f(x, y)

cplus = my2curry plus
inc' = cplus 1

-- ce is curried expression
my2uncurry :: (a -> b -> c) -> (a, b) -> c
my2uncurry ce (x, y) = ce x y

ucplus = my2uncurry cplus

unfold :: (t -> Bool) -> (t -> a) -> (t -> t) -> t -> [a]
unfold p h t x | p x = []
               | otherwise = h x : unfold p h t (t x)

int2bin' :: Int -> [Bit]
int2bin' = unfold (== 0) (`mod` 2) (`div` 2)

chop8' :: [Bit] -> [[Bit]]
chop8' = unfold null (take 8) (drop 8)

myMap' :: (a -> b) -> [a] -> [b]
myMap' f = unfold null (f . head) tail

myIterate' :: (a -> a) -> a -> [a]
myIterate' f x = x : (unfold (\_ -> False) f f x)
