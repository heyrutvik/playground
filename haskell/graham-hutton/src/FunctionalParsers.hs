module FunctionalParsers where

import Prelude hiding ((>>=), return)

type Parser a = String -> [(a, String)]

return :: a -> Parser a
return v = \x -> [(v, x)]

failure :: Parser a
failure = \_ -> []

item :: Parser Char
item = \x -> case x of
               [] -> []
               (x : xs) -> [(x, xs)]

parse :: Parser a -> String -> [(a, String)]
parse p x = p x

-- (item >>= (\x -> return (ord x)) >>= (\x -> return (x - ord 'a'))) "bc"
-- (item >>= \x -> item >>= \y -> return (x, y)) "abc"
(>>=) :: Parser a -> (a -> Parser b) -> Parser b
p >>= f = \x -> case parse p x of
                     [] -> []
                     [(v, out)] -> parse (f v) out


-- "abcde" -> [('a', 'c'), "de"]
p' :: Parser (Char, Char)
p' = item >>= (\x -> item >>= (\y -> item >>= (\z -> return (x, z))))

-- "abcde" -> [('a', 'c'), "de"]
p'' :: Parser (Char, Char)
p'' = item >>= \x ->
      item >>= \y ->
      item >>= \z ->
      return (x, z)

{-
src/FunctionalParsers.hs:43:8: error:
    • Couldn't match type ‘[(Char, String)]’ with ‘Char’
      Expected type: String -> [((Char, Char), String)]
        Actual type: Parser ([(Char, String)], [(Char, String)])
    • In a stmt of a 'do' block: return (x, z)
      In the expression:
        do x <- item
           y <- item
           z <- item
           return (x, z)
      In an equation for ‘p’:
          p = do x <- item
                 y <- item
                 z <- item
                 ....
   |
43 |        return (x, z)
   |        ^^^^^^^^^^^^^
-}
p :: Parser (Char, Char)
p = do x <- item
       y <- item
       z <- item
       return (x, z)
