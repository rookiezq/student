# 挂科数目和去向比较

import numpy as np
import pandas as pd
import matplotlib as mpl
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn import preprocessing, model_selection
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.pipeline import Pipeline
from sklearn.linear_model import LogisticRegression

sns.set(style="darkgrid", color_codes=True)
# 指定默认字体 不设置中文会显示有问题
mpl.rcParams['font.sans-serif'] = ['SimHei']
mpl.rcParams['axes.unicode_minus'] = False

# 读取本地数据
filename1 = './src/main/resources/python/计科主要课程及去向.csv'
filename2 = './src/main/resources/python/会计主要课程及去向.csv'
data1 = pd.read_csv(filename1)
data2 = pd.read_csv(filename2)

path = 'src/main/resources/static/img/'

# df = None
# 存放挂科数num
# nums = []
# 存放毕业去向与对应的挂科数
# counts = [0, 0, 0]


def read(dataframe):
    df = dataframe
    # 存放挂科数num
    nums = []

    # 统计挂科数
    def fail_course():
        # 遍历行
        for i in range(len(df)):
            # 挂科数
            num = 0
            # 列
            for j in range(df.shape[1]):
                x = df.iloc[i, j]
                # 如果二者都包含 则分割◇
                if type(x) == str and ('□' in x or '◇' in x):
                    # print(x)
                    num += 1
            nums.append(num)

    # 数据清洗
    def data_clean():
        # 遍历行
        for i in range(len(df)):
            # 列
            for j in range(df.shape[1]):
                x = df.iloc[i, j]
                # 如果二者都包含 则分割◇
                if type(x) == str and '□' in x and '◇' in x:
                    df.iloc[i, j] = x.split('◇')[0]
                elif type(x) == str and '□' in x:
                    df.iloc[i, j] = x.split('□')[0]
                elif type(x) == str and '◇' in x:
                    df.iloc[i, j] = x.split('◇')[0]

    # 统计每个学生挂科数目
    fail_course()
    ser = pd.Series(nums)
    df['挂科数'] = ser
    # 数据清洗
    # 如图中高等数据I(1) 4行 55□86 代表挂科 将类似所有的数据取第一次的分数（即挂科的分数）
    # 判断条件为是否包含 □ 或者 ◇
    # print("行数为", len(df))
    # print("列数为", df.shape[1])
    data_clean()
    # 将所有字符串转为float
    df.iloc[:, 1:17] = df.iloc[:, 1:17].astype(float)
    df['平均分'] = df.iloc[:, 1:17].mean(axis=1)
    # 对小数四舍五入 保留一位
    df['平均分'].round(decimals=1)

    # 新建一个连续的索引代替学号
    df['索引'] = range(1, len(df) + 1)
    return df


# 这里labels可加可不加 因为dataframe自动进行归类
labels = ['出国', '升学', '就业']
# 将某部分爆炸出来， 使用括号，将第一块分割出来，数值的大小是分割出来的与其他两块的间隙
explode = (0, 0.1, 0)
# 颜色
colors = ['r', 'yellowgreen', 'lightskyblue']
# 字体大小
fontsize = 20


# 对两个专业毕业去向一起统计并绘制饼图
def analyse_two(df1, df2):
    # 划分为一行两列
    fig, ax = plt.subplots(1, 2)
    fig.set_figheight(10)
    fig.set_figwidth(15)
    # 对两个专业毕业去向进行统计
    count_classes1 = pd.value_counts(df1.iloc[:, 17]).sort_index()
    count_classes2 = pd.value_counts(df2.iloc[:, 17]).sort_index()
    # ！！！fontsize 设置字体大小 这是相比plot的优势所在 也就是为什么不直接用plot
    count_classes1.plot.pie(ax=ax[0], autopct='%.1f%%', explode=explode, colors=colors, labels=labels,
                            fontsize=fontsize,
                            shadow=False, labeldistance=1.1, pctdistance=0.5, startangle=90)
    count_classes2.plot.pie(ax=ax[1], autopct='%.1f%%', explode=explode, colors=colors, labels=labels,
                            fontsize=fontsize,
                            shadow=True, labeldistance=1.1, pctdistance=0.5, startangle=90)
    # labeldistance，文本的位置离远点有多远，1.1指1.1倍半径的位置 默认1.1
    # autopct，圆里面的文本格式，%1.1f%%表示小数有三位，整数有一位的浮点数
    # shadow，饼是否有阴影
    # startangle，起始角度，0，表示从0开始逆时针转，为第一块。一般选择从90度开始比较好看
    # pctdistance，百分比的text离圆心的距离
    # patches, l_texts, p_texts，为了得到饼图的返回值，p_texts饼图内部文本的，l_texts饼图外label的文本

    # 设置子图的标题
    ax[0].set_title('计科毕业去向比例', fontsize=fontsize, bbox={'facecolor': '0.9', 'pad': 6})
    ax[1].set_title('会计毕业去向比例', fontsize=fontsize, bbox={'facecolor': '0.9', 'pad': 6})
    ax[0].set_ylabel('')
    ax[1].set_ylabel('')
    # 设置x，y轴刻度一致，这样饼图才能是圆的
    # plt.axis('equal')
    plt.legend(fontsize=fontsize, loc=0, bbox_to_anchor=(0, 1.0), borderaxespad=0.)
    # 铺满整个画布 防止保存图片不完整
    plt.tight_layout()
    # 保存在当前目录
    plt.savefig(path + "计科和会计去向比例.jpg")
    #plt.show()


# 对某个专业毕业去向一起统计并绘制饼图
def analyse(dataframe, maj):
    plt.figure(figsize=(8, 6), dpi=150)
    # 划分为一行两列
    # 对两个专业毕业去向进行统计
    count_classes = pd.value_counts(dataframe.iloc[:, 17]).sort_index()
    # ！！！fontsize 设置字体大小 这是相比plot的优势所在 也就是为什么不直接用plot
    count_classes.plot.pie(autopct='%.1f%%', explode=explode, colors=colors, labels=labels,
                           fontsize=25,
                           shadow=True, labeldistance=1.1, pctdistance=0.5, startangle=90)
    # labeldistance，文本的位置离远点有多远，1.1指1.1倍半径的位置 默认1.1
    # autopct，圆里面的文本格式，%1.1f%%表示小数有三位，整数有一位的浮点数
    # shadow，饼是否有阴影
    # startangle，起始角度，0，表示从0开始逆时针转，为第一块。一般选择从90度开始比较好看
    # pctdistance，百分比的text离圆心的距离
    # patches, l_texts, p_texts，为了得到饼图的返回值，p_texts饼图内部文本的，l_texts饼图外label的文本

    # 设置子图的标题
    plt.title(maj + '毕业去向比例', fontsize=25, bbox={'facecolor': '0.9', 'pad': 6})
    plt.ylabel('')
    # 设置x，y轴刻度一致，这样饼图才能是圆的
    # plt.axis('equal')
    plt.legend(fontsize=fontsize,loc=0, bbox_to_anchor=(0.9, 1.0))
    # 铺满整个画布 防止保存图片不完整
    plt.tight_layout()
    # 保存在当前目录
    plt.savefig(path + maj + "毕业去向比例.jpg")
    #plt.show()


# 建模
def creat_model(dataframe):
    df = dataframe
    # 将特征集（主要课程）与结果集（毕业去向）分割 1:17为特征集 17为结果集
    X, y = df.values[:, 1:17], df.values[:, 17]
    # 对结果集进行分类数值化
    #  {就业：2，出国：0，升学：1}
    encoder = LabelEncoder()
    y = encoder.fit_transform(y)
    # 在 X中取[0,3](高等数学Ⅰ（1）和面向对象程序设计)作为特征（为了后期的可视化画图更加直观，故只取前两列特征值向量进行训练）
    x = X[:, :].astype("float64")
    # 将特征集和结果集分别分为训练集和测试集 测试集比例系数test_size=0.3
    x_train, x_test, y_train, y_test = model_selection.train_test_split(x, y, random_state=1, test_size=0.25)
    # random_state为随即种子 赋值后保证每次取的数据集相同 如下面的例子
    # random_state=1
    # print(x_train[:3,:]) #只打印前三行
    # 结果为
    # [[77.0 79.0]
    #  [55.0 83.0]
    #  [75.0 69.0]]
    # 如果random_state=0
    # x_train,x_test,y_train,y_test=model_selection.train_test_split(x,y,random_state=0,test_size=0.3)
    # print(x_train[:3,:]) #只打印前三行
    # 结果为
    # [[75.0 83.0]
    #  [78.0 76.0]
    #  [93.0 63.0]]
    # （3）搭建模型，训练LogisticRegression分类器
    # 不加参数的训练集准确率和测试集准确率低 且有警告
    # classifier = Pipeline([('sc', StandardScaler()), ('clf', LogisticRegression())])
    # LogisticRegression-输出训练集的准确率为： 0.8095238095238095
    # LogisticRegression-输出测试集的准确率为： 0.6888888888888889

    # 加了参数的准确率高 且无警告
    # LogisticRegression-输出训练集的准确率为： 0.8285714285714286
    # LogisticRegression-输出测试集的准确率为： 0.7777777777777778
    classifier = Pipeline([('sc', StandardScaler()), (
        'clf', LogisticRegression(penalty='l2', C=0.1, solver='lbfgs', multi_class='multinomial', max_iter=100))])
    # 开始训练
    classifier.fit(x_train, y_train.ravel())

    # （4）计算LogisticRegression分类器的准确率
    #print("LogisticRegression-输出训练集的准确率为：", classifier.score(x_train, y_train))
    y_hat = classifier.predict(x_train)
    #print('预测结果集', y_hat)
    #print('真实结果集', y_train)
    #print("LogisticRegression-输出测试集的准确率为：", classifier.score(x_test, y_test))
    y_hat = classifier.predict(x_test)
    #print('预测结果集', y_hat)
    #print('真实结果集', y_test)

    # 从sklearn.metrics里导入classification_report模块。
    from sklearn.metrics import classification_report
    # 利用classification_report模块获得LogisticRegression其他三个指标的结果。
    #print('\n打印report')
    #print(classification_report(y_test, y_hat, target_names=labels))

    return classifier


# 对给定的成绩进行预测并绘制饼图
def predict(classifier, data):
    narray = np.array(data)
    d = narray.reshape(1, -1)
    s = classifier.predict_proba(d)
    np.set_printoptions(precision=3, suppress=True)
    print(s[0])
    df1 = pd.DataFrame(s[0], index=labels)
    df1.iloc[:, 0].plot.pie(figsize=(15, 10), autopct='%.1f%%', explode=explode, colors=colors, labels=labels,
                            fontsize=25, shadow=True, labeldistance=1.1, pctdistance=0.5, startangle=90)
    type(df1.iloc[:, 0])
    plt.legend(fontsize=fontsize, loc=0, borderaxespad=0.)
    plt.ylabel('')
    plt.title('毕业去向预测', fontsize=fontsize, loc='center')
    # 铺满整个画布 防止保存图片不完整
    plt.tight_layout()
    # 保存在当前目录
    plt.savefig(path + "predict.jpg")
    #plt.show()


# 去向和对应挂科数统计
def future_fail_course(dataframe):
    # 存放毕业去向与对应的挂科数
    # counts = np.zeros((1, 3))
    #     counts = [0, 0, 0]
    #     df = dataframe
    #     # 遍历行
    #     for i in range(len(df)):
    #         # 挂科数
    #         kind = df.iloc[i, 17]
    #         fail_num = df.iloc[i, 18]

    #         if kind == '出国':
    #             counts[0] += fail_num
    #         elif kind == '升学':
    #             counts[1] += fail_num
    #         elif kind == '就业':
    #             counts[2] += fail_num
    group = dataframe['挂科数'].groupby(dataframe['毕业去向'])
    group = group.sum()
    return list(group)


# 毕业去向与挂科数的柱状图
def future_fail_course_bar(counts):
    index = np.arange(3)
    plt.figure(figsize=(8, 6), dpi=120)
    plt.bar(index, counts[0], color='#9b98fd', tick_label=labels, width=0.35, label='计科')
    plt.bar(index + 0.35, counts[1], color='#ff999a', tick_label=labels, width=0.35, label='会计')
    plt.xticks(index + 0.35 / 2)
    # 设置横轴标签
    plt.xlabel('毕业去向', fontsize=fontsize)
    # 设置纵轴标签,
    plt.ylabel('挂科数', fontsize=fontsize)
    plt.tick_params(labelsize=15)
    plt.legend(fontsize=fontsize)
    # 添加标题
    plt.title('毕业去向与挂科数', fontsize=fontsize)
    # 铺满整个画布 防止保存图片不完整
    plt.tight_layout()
    # 保存在当前目录
    plt.savefig(path + "毕业去向和挂科数.jpg")
    #plt.show()


# 毕业去向和均分分组统计
def future_avgscore(dataframe):
    df = dataframe
    # 这里不能用学号作为x轴 因为学号不是连续的 而且还存在转专业的情况
    # 看上面read()方法最后几行
    group = df[['毕业去向', '索引', '平均分']].groupby(['毕业去向'])
    return list(group)


# 毕业去向和均分分组统计的散点图
def future_avgscore_scatter(group, maj):
    # 自行打印group查看结构
    # print(group)
    # group[0]代表出国 group[1]代表升学 group[2]就业
    # group[0][1] 对应的dataframe结构 group[0][2]不存在
    df1 = group[0][1]
    df2 = group[1][1]
    df3 = group[2][1]
    # 总人数
    x1 = df1['索引']
    y1 = df1['平均分']
    x2 = df2['索引']
    y2 = df2['平均分']
    x3 = df3['索引']
    y3 = df3['平均分']
    plt.figure(figsize=(8, 6), dpi=150)
    # ax = plt.subplot()
    plt.scatter(x1, y1, s=100, alpha=0.5, label='出国', edgecolors='black')
    plt.scatter(x2, y2, s=100, marker='x', c='green', alpha=0.6, label='升学', edgecolors='black')
    plt.scatter(x3, y3, s=100, marker='v', c='y', alpha=0.6, label='就业', edgecolors='black')
    # x1 横坐标 y1纵坐标
    # s点的大小 alpha透明度 区间0~1 数值越低透明度越高
    # c颜色 label 标题名称
    # edgecolors 点外边框颜色

    # 设置横轴标签
    plt.xlabel('学号', fontsize=fontsize)
    # 设置纵轴标签,
    plt.ylabel('平均分', fontsize=fontsize)
    plt.tick_params(labelsize=15)
    plt.legend(fontsize=fontsize, bbox_to_anchor=(1, 1.0), )
    # 添加标题
    plt.title(maj + '毕业去向与平均分', fontsize=fontsize)
    # 铺满整个画布 防止保存图片不完整
    plt.tight_layout()
    # 保存在当前目录
    plt.savefig(path + maj + "毕业去向和平均分.jpg")
    #plt.show()


# 毕业去向和均分分组统计的散点图
def future_avgscore_scatter_two(dataframe):
    df = dataframe
    df.loc[:, ['学号', '索引', '专业', '挂科数', '毕业去向', '平均分']].head()
    plt.figure(figsize=(8, 6), dpi=150)
    sns.swarmplot(x="毕业去向", y="平均分", hue='专业', data=df)
    # 设置横轴标签
    plt.xlabel('毕业方向', fontsize=fontsize)
    # 设置纵轴标签,
    plt.ylabel('平均分', fontsize=fontsize)
    plt.tick_params(labelsize=15)
    plt.legend(fontsize=fontsize)
    # 添加标题
    plt.title('毕业去向与平均分', fontsize=fontsize)
    # 铺满整个画布 防止保存图片不完整
    plt.tight_layout()
    # 保存在当前目录
    plt.savefig(path + "毕业去向和平均分.jpg")
    #plt.show()


def avgscore_two(dataframe):
    df = dataframe
    plt.figure(figsize=(15, 6), dpi=150)
    sns.pointplot(x="索引", y="平均分", hue="专业", data=df, kde=False, markers=["^", "o"],
                  palette={"计科": "g", "会计": "m"}, linestyles=["-", "--"])
    # plt.xticks([])
    # 设置横轴标签
    plt.xlabel('学号', fontsize=fontsize)
    # 设置纵轴标签,
    plt.ylabel('平均分', fontsize=fontsize)
    plt.tick_params(labelsize=15)
    # plt.legend(fontsize=fontsize)
    # 添加标题
    plt.title('计科和会计的平均分', fontsize=fontsize)
    plt.legend(fontsize=fontsize)
    # 铺满整个画布 防止保存图片不完整
    plt.tight_layout()
    # 保存在当前目录
    plt.savefig(path + "计科和会计的平均分.jpg")
    #plt.show()
