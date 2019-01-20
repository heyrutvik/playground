module InteractivePrograms where
{-
(>>=) :: IO a -> (a -> IO b) -> IO b
f >>= g = \world -> case f world of
                  (v, world') -> g v world'
-}
