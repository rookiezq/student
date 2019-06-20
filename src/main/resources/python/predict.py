import student as stu
import sys

df1 = stu.read(stu.data1)
df2 = stu.read(stu.data2)

# 默认df = df1
df = df1

result = ['计科', '会计']
# 对应三种结果'计科','会计','计科和会计'
arg = sys.argv[1]
arg2 = sys.argv[2]
data = [float(x) for x in arg2.split(',')]
if arg == '0':
    df = df1
elif arg == '1':
    df = df2

classifier = stu.creat_model(df)
stu.predict(classifier, data)
