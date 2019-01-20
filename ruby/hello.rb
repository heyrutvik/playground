print "Hello, World!\n"

a, b, c = [1,2,3]
print b.to_s + "\n"

module Add
  def add(x, y)
    x + y
  end
  def not(x)
    -x
  end
end

module Subtract
  def subtract(x, y)
    x - y
  end
end

class Calculator include Subtract, Add
  def divide(x, y)
    x / y
  end
end

def join_with_commas(*words)
  words.join(", ")
end

print join_with_commas("1", "3", "2") + "\n"

def do_three_times
  yield("1")
  yield("2")
  yield("3")
end

do_three_times { |n| puts "#{n}: hello" }

class Point < Struct.new(:x, :y)
  def +(other_point)
    Point.new(x + other_point.x, y + other_point.y)
  end
  def inspect
    "<Point (#{x}, #{y})>"
  end
end

class Point
  def -(other_point)
    Point.new(x - other_point.x, y - other_point.y)
  end
end

class Tag
  NAME = "tag name"
end

p = Point.new(1,1)
print p.to_s + "\n"
print (p + Point.new(2,2)).to_s + "\n"
print (p - Point.new(1,1)).to_s + "\n"
p1 = Point.new(3,4)
print (p1 - Point.new(1,1)).to_s + "\n"

print Tag::NAME + "\n"

def reduce(arr, z, f)
  arr.inject(z) { |r, n| f.call(r, n) }
end

sum = -> x, y { x + y }
six = reduce([1,2,3], 0, sum)
print six.to_s + "\n"
