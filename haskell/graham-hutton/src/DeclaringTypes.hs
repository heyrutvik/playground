module DeclaringTypes where

-- type declarations
type String = [Char]

type Board = [Pos]

type Pos = (Int, Int)

-- type Tree = (Int, [Tree]) // use `data` for recusrive type
type Assoc k v = [(k, v)]

find :: Eq k => k -> Assoc k v -> v
find k t = head [v | (k', v) <- t, k == k']

-- data declarations
data Animal
  = Dog
  | Cat

printAnimal :: Animal -> IO ()
printAnimal Dog = print "dog"
printAnimal Cat = print "cat"
