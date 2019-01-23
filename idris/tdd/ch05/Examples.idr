module Examples

import Data.Vect

readVectLen : (n : Nat) -> IO (Vect n String)
readVectLen Z = pure []
readVectLen (S k) = do
  x <- getLine
  xs <- readVectLen k
  pure (x :: xs)

data VectUnknown : Type -> Type where
  MkVect : (n : Nat) -> Vect n t -> VectUnknown t

readVect : IO (VectUnknown String)
readVect = do
  x <- getLine
  if (x == "")
     then pure (MkVect _ [])
     else do MkVect _ xs <- readVect
             pure (MkVect _ (x :: xs))

printVect : Show a => VectUnknown a -> IO ()
printVect (MkVect n xs) = putStrLn (show xs ++ " (length " ++ show n ++ ")")

readVectE : IO (len ** Vect len String)
readVectE = do
  x <- getLine
  if (x == "")
     then pure (_ ** [])
     else do (_ ** xs) <- readVectE
             pure (_ ** x :: xs)

zipInputs : IO ()
zipInputs = do
  putStrLn "Enter first vector: "
  (len1 ** vect1) <- readVectE
  putStrLn "Enter second vector: "
  (len2 ** vect2) <- readVectE
  case exactLength len1 vect2 of
    Nothing => putStrLn "Vectors are different lengths"
    Just vect2' => printLn (zip vect1 vect2')
