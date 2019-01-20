module FirstSteps where

add x y = x + y

{-
(2 ^ 3) * 4
(2 * 3) + (4 * 5)
2 + (3 * (4 ^ 5))
-}
n = a `div` length xs
  where
    a = 10
    xs = [1, 2, 3, 4, 5]

-- `last` using other library functions
myLast xs = head (reverse xs)

-- poor way to imitate `last` function
myLast' []     = []
myLast' (x:[]) = x
myLast' (x:xs) = myLast' xs

-- `init` using other library function
myInit xs = reverse (tail (reverse xs))

myInit' xs = take (length xs - 1) xs

myInit'' xs = _myInit xs []
  where
    _myInit (x:[]) ns = reverse ns
    _myInit (x:xs) ns = _myInit xs (x : ns)
