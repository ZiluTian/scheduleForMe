这个软件为你量身定制私人时间表，依据你需要完成的任务以及预定的时间。

This project generates a schedule for you based on the tasks and durations that you specified, either through the terminal or defined at the default/routine file. The scheduling unit is in hour, so please enter 0.5 for 30 minutes. 

We support different scheduling modes, by default it is Fair (we interleave the  schedule of each task). You can optionally change it to Sequential (all tasks are performed one after another). 

To run the project in background, clone this folder and type
```bash run.sh &```

Possible output: 
```
时间表:
00:58 开始工作
02:18 工作结束
02:38 锻炼结束
02:48 休息结束
04:08 工作结束
04:28 锻炼结束
04:38 休息结束
05:58 工作结束
06:18 锻炼结束
06:28 休息结束
```