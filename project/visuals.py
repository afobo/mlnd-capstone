import matplotlib.pyplot as plt
import seaborn as sns

def plot1(data):
    colors = ['red' if failed else 'green' for failed in data['failed']]
    plt.figure(figsize=(14,7))
    plt.title("Time to create vs. VM count")
    plt.xlabel("VM count")
    plt.ylabel("Seconds")
    plt.scatter(x='vm_count', y='seconds', data=data, marker='o', sizes=data['attempts']*30, c=colors, alpha=0.5)
    plt.show()

def plot2(data):
    fig, ax = plt.subplots(figsize=(14,7))
    sns.boxplot(x='vm_count', y= 'seconds', data=data, ax=ax)
    ax.set_xlabel("VM count")
    ax.set_ylabel("Seconds")

def plot3(data):
    fig, ax = plt.subplots(figsize=(14,7))
    sns.scatterplot(x="vm_count", y="seconds", hue="failed", size="attempts", data=data,
                    ax=ax, sizes=(50,200), alpha=0.5, palette=['green', 'red'], legend='full')
    ax.set_xlabel("VM count")
    ax.set_ylabel("Seconds")
    ax.set_title("Time to create vs. VM count")
