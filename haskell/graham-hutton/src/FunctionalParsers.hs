module FunctionalParsers where

import           Data.Char
import           Prelude   hiding (return, (>>=))

type Parser a = String -> [(a, String)]

return :: a -> Parser a
return v = \x -> [(v, x)]

failure :: Parser a
failure = \_ -> []

item :: Parser Char
item =
  \x ->
    case x of
      []     -> []
      (x:xs) -> [(x, xs)]

parse :: Parser a -> String -> [(a, String)]
parse p x = p x

-- (item >>= (\x -> return (ord x)) >>= (\x -> return (x - ord 'a'))) "bc"
-- (item >>= \x -> item >>= \y -> return (x, y)) "abc"
(>>=) :: Parser a -> (a -> Parser b) -> Parser b
p >>= f =
  \x ->
    case parse p x of
      []         -> []
      [(v, out)] -> parse (f v) out

-- "abcde" -> [('a', 'c'), "de"]
p :: Parser (Char, Char)
p = item >>= (\x -> item >>= (\y -> item >>= (\z -> return (x, z))))

-- "abcde" -> [('a', 'c'), "de"]
p' :: Parser (Char, Char)
p' = item >>= \x -> item >>= \y -> item >>= \z -> return (x, z)

{-
p'' :: Parser (Char, Char)
p'' = do x <- item
       y <- item
       z <- item
       return (x, z)
-}
(+++) :: Parser a -> Parser a -> Parser a
p +++ q =
  \x ->
    case parse p x of
      []         -> parse q x
      [(v, out)] -> [(v, out)]

-- could be done with do notation
sat :: (Char -> Bool) -> Parser Char
sat p =
  item >>= \x ->
    if p x
      then return x
      else failure

digit :: Parser Char
digit = sat isDigit

lower :: Parser Char
lower = sat isLower

upper :: Parser Char
upper = sat isUpper

letter :: Parser Char
letter = sat isAlpha

alphanum :: Parser Char
alphanum = sat isAlphaNum

char :: Char -> Parser Char
char x = sat (== x)

string :: String -> Parser String
string []     = return []
string (x:xs) = char x >>= (\_ -> string xs >>= (\_ -> return (x : xs)))

many :: Parser a -> Parser [a]
many p = many1 p +++ return []

many1 :: Parser a -> Parser [a]
many1 p = p >>= (\v -> many p >>= (\vs -> return (v : vs)))

ident :: Parser String
ident = lower >>= (\x -> many alphanum >>= (\xs -> return (x : xs)))

nat :: Parser Int
nat = many1 digit >>= (\xs -> return (read xs))

space :: Parser ()
space = many (sat isSpace) >>= (\_ -> return ())

token :: Parser a -> Parser a
token p = space >>= (\_ -> p >>= (\v -> space >>= (\_ -> return v)))

identifier :: Parser String
identifier = token ident

natural :: Parser Int
natural = token nat

symbol :: String -> Parser String
symbol xs = token (string xs)

p''' :: Parser [Int]
p''' =
  symbol "[" >>= \_ ->
    natural >>= \n ->
      many (symbol "," >>= \_ -> natural) >>= \ns ->
        symbol "]" >>= \_ -> return (n : ns)

{-
expr = term ("+" expr | "-" expr | @)
term = power ("*" term | "/" term | @)
power = factor ("^" power | @)
factor = "(" expr ")" | nat
nat = 0 | 1 | 2 | 3 | ...

where @ = empty string
-}
expr :: Parser Int
expr =
  term >>= \t ->
    ((symbol "+" >>= \_ -> expr >>= \e -> return (t + e)) +++
     (symbol "-" >>= \_ -> expr >>= \e -> return (t - e)) +++ return t)

term :: Parser Int
term =
  power >>= \f ->
    ((symbol "*" >>= \_ -> term >>= \t -> return (f * t)) +++
     (symbol "/" >>= \_ -> term >>= \t -> return (f `div` t)) +++ return f)

power :: Parser Int
power =
  factor >>= \f ->
    (symbol "^" >>= \_ -> power >>= \t -> return (f ^ t)) +++ return f

factor :: Parser Int
factor =
  (symbol "(" >>= \_ -> expr >>= \e -> symbol ")" >>= \_ -> return e) +++
  natural

eval :: String -> Int
eval = mkEval expr

mkEval :: Parser Int -> String -> Int
mkEval p s =
  case parse p s of
    [(n, [])]  -> n
    [(_, out)] -> error ("unconsumed input " ++ out)
    []         -> error "invalid input"

-- Exercises
int :: Parser Int
int = (symbol "-" >>= \_ -> natural >>= \n -> return (-n)) +++ natural

newline :: Parser Char
newline = char '\n'

comment :: Parser ()
comment =
  symbol "--" >>= \_ ->
    many alphanum >>= \_ -> (newline >>= \_ -> return ()) +++ failure

{-
expr = expr - nat | nat
nat = 0 | 1 | 2 | 3 | ...
-}
-- loop forever becuase `expr` defined recusrivly on its left side
expr' :: Parser Int
expr' =
  expr' >>= \e ->
    symbol "-" >>= \_ -> natural >>= \n -> return (e - n) +++ natural

eval' :: String -> Int
eval' = mkEval expr'

-- take number and then repaet `"-" nat` pattern and fold using subtract operation
expr'' :: Parser Int
expr'' =
  natural >>= \n ->
    (many (symbol "-" >>= \_ -> natural)) >>= \ns -> return (foldl (-) n ns)

eval'' :: String -> Int
eval'' = mkEval expr''
