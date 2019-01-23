module Exercises

data Format = Number Format
            | Str Format
            | Lit String Format
            | Cha Format
            | Flo Format
            | End

ex : Format
ex = Str (Lit " = " (Number End))

PrintfType : Format -> Type
PrintfType (Number fmt) = Int -> PrintfType fmt
PrintfType (Str fmt) = String -> PrintfType fmt
PrintfType (Cha fmt) = Char -> PrintfType fmt
PrintfType (Flo fmt) = Double -> PrintfType fmt
PrintfType (Lit x fmt) = PrintfType fmt
PrintfType End = String

printfFormat : (fmt : Format) -> String -> PrintfType fmt
printfFormat (Number fmt) acc = \n => printfFormat fmt (acc ++ show n)
printfFormat (Str fmt) acc = \s => printfFormat fmt (acc ++ s)
printfFormat (Cha fmt) acc = \c => printfFormat fmt (acc ++ (show c))
printfFormat (Flo fmt) acc = \d => printfFormat fmt (acc ++ (show d))
printfFormat (Lit lit fmt) acc = printfFormat fmt (acc ++ lit)
printfFormat End acc = acc

toFormat : (xs : List Char) -> Format
toFormat [] = End
toFormat ('%' :: 'd' :: chars) = Number (toFormat chars)
toFormat ('%' :: 's' :: chars) = Str (toFormat chars)
toFormat ('%' :: 'c' :: chars) = Cha (toFormat chars)
toFormat ('%' :: 'f' :: chars) = Flo (toFormat chars)
toFormat ('%' :: chars) = Lit "%" (toFormat chars)
toFormat (c :: chars) = case toFormat chars of
                             (Lit lit fmt) => Lit (strCons c lit) fmt
                             fmt => Lit (strCons c "") fmt

printf : (fmt : String) -> PrintfType (toFormat (unpack fmt))
printf fmt = printfFormat _ ""

TupleVect : Nat -> Type -> Type
TupleVect Z ty = ()
TupleVect (S k) ty = (ty, TupleVect k ty)
