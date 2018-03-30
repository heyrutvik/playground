module Main

main : IO ()
main = putStrLn (?convert 'x')

twice : (a -> a) -> a -> a
twice f x = f (f x)

pythagoras : Double -> Double -> Double
pythagoras x y = sqrt (square x + square y)
  where
    square : Double -> Double
    square x = x * x

StringOrInt : Bool -> Type
StringOrInt x = case x of True => Int
                          False => String

getStringOrInt : (x : Bool) -> StringOrInt x
getStringOrInt x = case x of True => 94
                             False => "Ninety four"

valToString : (x : Bool) -> StringOrInt x -> String
valToString x val = case x of True => cast val
                              False => val

xor : Bool -> Bool -> Bool
xor False y = y
xor True y = not y
