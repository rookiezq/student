import student as stu
import sys

df1 = stu.read(stu.data1)
df2 = stu.read(stu.data2)
np = stu.np
pd = stu.pd
df1['专业'] = '计科'
df2['专业'] = '会计'
# 将两个专业数据拼接
df = pd.concat([df1, df2], ignore_index=True, sort=True)

result = ['计科', '会计', '计科和会计']
# 对应三种结果'计科','会计','计科和会计'
arg = sys.argv[1]
# 计科
if arg == '0':
    stu.analyse(df1, result[0])
    group = stu.future_avgscore(df1)
    stu.future_avgscore_scatter(group, result[0])
# 会计
elif arg == '1':
    stu.analyse(df2, result[1])
    group = stu.future_avgscore(df2)
    stu.future_avgscore_scatter(group, result[1])
# 计科和会计
elif arg == '2':
    # 分析两个专业去向比例
    stu.analyse_two(df1, df2)
    # 先取出每个专业对应的挂科数
    counts = [[]]
    counts1 = stu.future_fail_course(df1)
    counts2 = stu.future_fail_course(df2)
    counts[0] = counts1
    counts.append(counts2)
    counts = np.array(counts)
    # 分析两个专业毕业去向和对应的挂科数
    stu.future_fail_course_bar(counts)
    # 分析两个专业的平均分
    stu.avgscore_two(df)
    # 分析两个专业毕业去向和均分的关系
    stu.future_avgscore_scatter_two(df)
