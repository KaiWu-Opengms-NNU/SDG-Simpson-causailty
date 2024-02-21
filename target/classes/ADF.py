import statsmodels.tsa.stattools as ts
import sys

def ADF(x):
    result = ts.adfuller(x)
    return result


if __name__ == '__main__':
    a=[]
    for i in range(1,len(sys.argv)):
        a.append(sys.argv[i])
    print(ADF(a))