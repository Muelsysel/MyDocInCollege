#include <string>
#include <sstream>
#include <iostream>

#include "disk.h"

using namespace std;

HardDisk::HardDisk() : direction(Inc), current_process(-1), current_track(0), io_queue(0) {}

HardDisk::~HardDisk() {}

// 具有给定pid的进程请求使用磁盘读取/写入编号为track_no的磁道。
// 按照磁盘调度算法将不同进程的IO请求插入该磁盘的IO队列中。 
void HardDisk::Request(const int track_no, const int &pid)
{
    if (DiskIsIdle())
    {
        SetCurrentProcess(pid);
        current_track = track_no;
    }
    else
    {
        if (io_queue.size() == 0)
        {
            io_queue.push_back({ pid,track_no });
        }
        else
        {
            if (direction == Inc)
            {
                int mx_track_no = -1;
                for (auto p = io_queue.begin();p != io_queue.end();p++)
                {
                    if (current_track < track_no)  //递增时插入
                    {
                        if (p->second <= mx_track_no)  //开始递减,要插入的为最大磁道号，此时插入
                        {
                            io_queue.insert(p, { pid,track_no });
                            return;
                        }
                        else if (p->second > track_no)
                        {
                            io_queue.insert(p, { pid,track_no });
                            return;
                        }
                        mx_track_no = max(p->second, mx_track_no);
                    }
                    else                        //后半段
                    {
                        if (p->second < track_no)
                        {
                            io_queue.insert(p, { pid,track_no });
                            return;
                        }
                    }
                }
                io_queue.push_back({ pid,track_no });
            }
            else           //磁道号减小方向
            {
                int mn_track_no = INT_MAX;
                for (auto p = io_queue.begin();p != io_queue.end();p++)
                {
                    if (current_track > track_no)  //比当前位置小，在递减时插入
                    {
                        if (p->second >= mn_track_no)  //开始递增,要插入的为最小磁道号，此时插入
                        {
                            io_queue.insert(p, { pid,track_no });
                            return;
                        }
                        else if (p->second < track_no)
                        {
                            io_queue.insert(p, { pid,track_no });
                            return;
                        }
                        mn_track_no = min(p->second, mn_track_no);
                    }
                    else                        //后半段,递增时放入
                    {
                        if (p->second > track_no)
                        {
                            io_queue.insert(p, { pid,track_no });
                            return;
                        }
                    }
                }
                io_queue.push_back({ pid,track_no });
            }

        }

    }

}

// 将当前使用磁盘的进程从磁盘移除并放回就绪队列。
// 如果IO队列中还有其他进程在等待该磁盘，则按照磁盘调度算法选择一个进程使用该磁盘。
int HardDisk::RemoveProcess()
{
    int removed_process = current_process;
    //std::cout << "请编写程序并删除此行语句：将当前使用磁盘的进程从磁盘移除并放回就绪队列。" << std::endl;
    //-----------------
    if (io_queue.size() == 0)
    {
        current_process = -1;
    }
    else
    {
        SetCurrentProcess(io_queue.front().first);
        current_track = io_queue.front().second;
        io_queue.pop_front();

    }
    //-----------------
    return removed_process;

}


// 移除正在使用磁盘或在IO队列中的给定进程。
void HardDisk::Remove(const int &pid)
{
    // 若进程正在使用磁盘 
    if (current_process == pid)
    {
        RemoveProcess();
    }
    // 若进程在IO队列中 
    for (auto itr = io_queue.begin(); itr != io_queue.end(); itr++)
    {
        if ((*itr).first == pid)
        {
            io_queue.erase(itr);
        }
    }
}

// 返回正使用磁盘的进程的PID。
int HardDisk::GetCurrentProcess() const
{
    return current_process;
}

// 返回当前进程正在读取或写入的当前磁道号。 
int HardDisk::GetCurrentTrack() const
{
    return current_track;
}

// 设置当前使用磁盘的进程。
void HardDisk::SetCurrentProcess(const int &pid)
{
    current_process = pid;
}

// 测试磁盘当前是否空闲。
bool HardDisk::DiskIsIdle() const
{
    return current_process == -1;
}

// 输出当前磁盘的IO队列。
void HardDisk::PrintQueue() const
{
    for (auto itr = io_queue.begin(); itr != io_queue.end(); itr++)
    {
        std::cout << "<- [" << (*itr).first << " " << (*itr).second << "] ";
    }
    std::cout << std::endl;
}
