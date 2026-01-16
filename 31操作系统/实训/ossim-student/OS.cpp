#include <string>
#include <sstream>
#include <iostream>

#include "OS.h"

using namespace std;

OperatingSystem::OperatingSystem(int &number_of_hard_disks_, unsigned int &RAM_, unsigned int &page_size_) : CPU(1),
                                                                                                             number_of_processes(1),
                                                                                                             timestamp(0),
                                                                                                             number_of_hard_disks(number_of_hard_disks_),
                                                                                                             page_size(page_size_),
                                                                                                             RAM(RAM_),
                                                                                                             number_of_frames(RAM_ / page_size_),
                                                                                                             ready_queue(0),
                                                                                                             hard_disks(number_of_hard_disks_),
                                                                                                             frames(0)
{
    // 创建初始进程 
    PCB *process_1 = new PCB{number_of_processes};
    process_1->SetParent(1); // 第一个进程（进程1）没有父进程 
    all_processes[number_of_processes] = process_1;
    
    //初始化frames
    frames.resize(number_of_frames, nullptr);  // 为每个 Frame 分配 nullptr
    for (int i = 0; i < number_of_frames; ++i) {
        frames[i] = new Frame();  // 为每个 Frame 分配内存
    }


    // 初始化硬盘 
    for (int i = 0; i < number_of_hard_disks; i++)
    {
        HardDisk *disk_ = new HardDisk{};
        hard_disks[i] = disk_;
    }
}

OperatingSystem::~OperatingSystem()
{
    // 删除硬盘 
    for (auto disk : hard_disks)
    {
        delete disk;
    }
    // 删除页框 
    for (auto frame : frames)
    {
        delete frame;
    }
    // 删除所有PCB 
    for (auto PCB : all_processes)
    {
        delete PCB.second;
    }

    hard_disks.clear();
    frames.clear();
    all_processes.clear();
}

// 创建一个新进程并将其添加到就绪队列或 CPU（如果该队列为空）。
// 新进程的父进程为进程1 
void OperatingSystem::CreateProcess()
{
    // 更新系统的进程计数
    number_of_processes++;
    // 创建新进程的 PID

    int new_pid = number_of_processes;

    // 创建一个新的 PCB 对象，进程的父进程为进程 1
    PCB* new_process = new PCB(new_pid);
    new_process->SetParent(1);  // 设置父进程为进程 1

    // 将新进程添加到所有进程的映射中
    all_processes[new_pid] = new_process;

    // 将新进程添加到就绪队列或直接分配给 CPU
    if (ready_queue.empty()) {
        // 如果就绪队列为空，直接分配该进程到 CPU
        CPU = new_pid;
    }
    else {
        // 否则，将新进程添加到就绪队列
        ready_queue.push_back(new_pid);
    }

    // 输出创建进程的相关信息
    std::cout << "创建进程 PID " << new_pid << " (父进程 PID: 1)" << std::endl;
}

// 创建一个子进程，其父进程是当前使用CPU的用户进程（若除了进程1之外没有其他进程，则报错）。
void OperatingSystem::Fork()
{
    //std::cout << "请编写程序并删除此行语句：建一个子进程，其父进程是当前使用CPU的用户进程（若除了进程1之外没有其他进程，则报错）。" << std::endl;

    // 如果当前没有进程在 CPU 上运行，或者除了进程 1 外没有其他进程，抛出错误
    if (CPU == -1 || all_processes.size() <= 1) {
        std::cerr << "Error: No processes to fork from, or only process 1 exists." << std::endl;
        return;
    }

    // 获取当前正在使用 CPU 的进程
    PCB* parent_process = all_processes[CPU];

    // 创建一个新的进程，新的进程的父进程为当前使用 CPU 的进程
    number_of_processes++;
    int new_pid = number_of_processes;
    

    // 创建子进程，并将父进程设置为当前进程（使用 CPU 的进程）
    PCB* new_process = new PCB(new_pid);
    new_process->SetParent(CPU);

    // 将子进程添加到当前进程的子进程列表
    parent_process->AddChildProcess(new_process);

    // 将新进程添加到进程映射中
    all_processes[new_pid] = new_process;

    // 将子进程添加到就绪队列中
    ready_queue.push_back(new_pid);

    // 输出创建子进程的相关信息
    std::cout << "创建子进程 PID " << new_pid << " (父进程 PID: " << CPU << ")" << std::endl;
}

// 将给定进程添加到就绪队列，或者如果 CPU 空闲，则该进程直接开始运行。
void OperatingSystem::AddToReadyQueue(const int pid)
{
    //std::cout << "请编写程序并删除此行语句：将给定进程添加到就绪队列，或者如果 CPU 空闲，则该进程直接开始运行。" << std::endl;	
    // 检查 CPU 是否空闲
    if (CPU == -1) {
        // CPU 空闲，直接分配给该进程
        CPU = pid;
        std::cout << "CPU 空闲，进程 " << pid << " 直接开始运行。" << std::endl;
    }
    else {
        // CPU 正在运行进程，将该进程加入就绪队列
        ready_queue.push_back(pid);
        std::cout << "进程 " << pid << " 被加入到就绪队列。" << std::endl;
    }
}

//按照RR调度算法处理就绪队列和当前使用CPU的进程。
void OperatingSystem::CPUToReadyQueue()
{
    //std::cout << "请编写程序并删除此行语句：按照RR调度算法处理就绪队列和当前使用CPU的进程。" << std::endl;
    // 如果 CPU 当前没有进程在运行
    if (CPU == -1) {
        std::cout << "CPU 当前没有进程在运行，无法进行调度。" << std::endl;
        return; // 不做任何操作
    }

    // 将当前使用 CPU 的进程放回就绪队列
    ready_queue.push_back(CPU);
    std::cout << "进程 " << CPU << " 被放回就绪队列。" << std::endl;

    // 从就绪队列取出下一个进程，分配给 CPU
    if (!ready_queue.empty()) {
        CPU = ready_queue.front();   // 获取队列中的第一个进程
        ready_queue.pop_front();     // 将该进程从队列中移除
        std::cout << "进程 " << CPU << " 被分配到 CPU 运行。" << std::endl;
    }
    else {
        // 如果就绪队列为空，表示没有进程可以调度
        CPU = -1;
        std::cout << "就绪队列为空，CPU 当前没有进程运行。" << std::endl;
    }

}

// 使用CPU的进程调用wait
// 如果进程没有子进程，则无需等待。 
// 如果进程已经拥有至少一个僵死子进程，则删除子进程并继续使用CPU。
// 如果进程没有僵死子进程，则该进程进入等待状态，并将就绪队列队首的进程发送给CPU。
void OperatingSystem::Wait()
{
    PCB *cpu_process = all_processes[CPU];// 获取当前使用 CPU 的进程

    //std::cout << "请编写程序并删除此行语句：占用CPU的进程调用wait。" << std::endl;

    if (cpu_process == nullptr) {
        std::cout << "没有找到当前使用 CPU 的进程。" << std::endl;
        return;
    }

    // 判断该进程是否有子进程
    if (cpu_process->HasChildren()) {
        // 检查是否有僵尸子进程
        int zombie_pid = cpu_process->ProcessHasZombieChild();
        if (zombie_pid != 0) {
            // 存在僵尸子进程，删除该子进程
            std::cout << "进程 " << zombie_pid << " 被删除。" << std::endl;
            PCB* zombie_process = all_processes[zombie_pid];
            cpu_process->RemoveChild(zombie_process); // 从父进程的子进程列表中删除
            delete zombie_process;  // 删除该僵尸子进程
        }
        else {
            // 如果没有僵尸子进程，进程进入等待状态
            std::cout << "进程 " << CPU << " 进入等待状态。" << std::endl;
            cpu_process->SetWaitingState(true); // 设置进程为等待状态
            CPU = -1; // 释放 CPU
            // 从就绪队列中取出下一个进程
            if (!ready_queue.empty()) {
                CPU = ready_queue.front();
                ready_queue.pop_front();
                std::cout << "进程 " << CPU << " 被分配到 CPU。" << std::endl;
            }
        }
    }
    else {
        std::cout << "进程 " << CPU << " 没有子进程，不需要等待。" << std::endl;
    }
}

// 当前正在使用CPU的进程终止
// 如果其父进程已在等待，则该进程立即终止，并且父进程将转到就绪队列的末尾。
// 如果父进程没有等待，该进程就会成为僵尸进程。 如果父进程是进程 1，则该进程立即终止。
// 该进程的所有子进程都被终止。
void OperatingSystem::Exit()
{
    // 获取当前正在使用CPU的进程
    PCB* exiting_process = all_processes[CPU];

    // 获取父进程
    PCB* parent = all_processes[exiting_process->GetParent()];

    // 如果没有找到当前使用的进程
    if (exiting_process == nullptr) {
        std::cout << "没有找到当前使用 CPU 的进程。" << std::endl;
        return;
    }

    //如果当前进程为1，直接退出程序
    if (exiting_process->GetPid() == 1)
    {
        exit(0);
    }

    // 如果父进程是进程 1，直接终止该进程
    if (parent->GetPid() == 1) {
        std::cout << "进程 " << exiting_process->GetPid() << " 终止。" << std::endl;




        // 终止该进程的所有子进程
        //for (PCB* child : exiting_process->GetChildren()) {
        //    std::cout << "进程 " << child->GetPid() << " 被终止。" << std::endl;
        //    //delete child;
        //    child->SetZombie(true);  // 设置子进程为僵尸进程
        //}
        DeleteChildren(exiting_process);
        // 从进程列表中删除该进程
        delete exiting_process;

        // 更新占用CPU的进程为就绪队列的下一个进程
        GetNextFromReadyQueue();

    }
    else {
        // 检查父进程是否在等待状态
        if (parent->IsWaiting()) {
            std::cout << "父进程 " << parent->GetPid() << " 等待状态，进程 " << exiting_process->GetPid() << " 终止。" << std::endl;

            // 终止该进程的所有子进程
            for (PCB* child : exiting_process->GetChildren()) {
                std::cout << "进程 " << child->GetPid() << " 被终止。" << std::endl;
                child->SetZombie(true);  // 设置子进程为僵尸进程
            }

            // 更新父进程并将其加入到就绪队列
            parent->SetWaitingState(false);  // 将父进程的等待状态设置为 false
            ready_queue.push_back(parent->GetPid());  // 将父进程加入到就绪队列末尾
            // 从父进程的子进程列表中移除自己
            if (parent != nullptr) {
                parent->RemoveChild(exiting_process);  // 确保父进程不会再引用已删除的子进程
            }
            // 删除当前进程
            all_processes.erase(exiting_process->GetPid());
            delete exiting_process;

            // 更新占用CPU的进程为就绪队列的下一个进程
            GetNextFromReadyQueue();
        }
        else {
            // 如果父进程没有等待，当前进程成为僵尸进程
            std::cout << "进程 " << exiting_process->GetPid() << " 成为僵尸进程。" << std::endl;
            exiting_process->SetZombie(true);  // 设置为僵尸进程

            // 更新占用CPU的进程为就绪队列的下一个进程
            GetNextFromReadyQueue();
        }
    }

}

// 当前正在使用CPU的进程请求对给定逻辑地址进行内存操作
void OperatingSystem::RequestMemoryOperation(const int &address)
{   

    // 获取当前使用CPU的进程
    PCB* cpu_process = all_processes[CPU];

    // 计算逻辑地址的页号
    int page_number = address / page_size;  // 假设每页的大小为 page_size

    // 检查进程的页表中是否有该页面
    bool page_found = false;
    for (auto frame : frames) {
        if (frame->pid_ == CPU && frame->page_ == page_number) {
            // 找到对应的页面，更新时间戳
            frame->timestamp_ = timestamp;
            page_found = true;
            break;
        }
    }

    // 如果页面未找到，进行分页调入
    if (!page_found) {
        bool page_loaded = false;

        // 如果内存还有空闲的页框，直接加载
        for (auto frame : frames) {
            if (frame->IsEmpty()) {
                frame->pid_ = CPU;
                frame->page_ = page_number;
                frame->timestamp_ = timestamp;
                page_loaded = true;
                break;
            }
        }

        // 如果内存已满，进行LRU置换
        if (!page_loaded) {
            // 使用LRU算法选择最久未使用的页面
            Frame* lru_frame = nullptr;
            int min_timestamp = INT_MAX;
            for (auto frame : frames) {
                if (frame->pid_ != 0 && frame->timestamp_ < min_timestamp) {
                    min_timestamp = frame->timestamp_;
                    lru_frame = frame;
                }
            }

            // 将最久未使用的页面替换
            if (lru_frame != nullptr) {
                // 先清除该页框
                lru_frame->Clear();

                // 将新页面加载到该页框
                lru_frame->pid_ = CPU;
                lru_frame->page_ = page_number;
                lru_frame->timestamp_ = timestamp;
            }
        }
    }
    // 更新时间戳
    timestamp++;
}

// 当前使用CPU的进程请求访问disk_number号硬盘的track_no号磁道
// 从CPU中移除当前进程并从就绪队列中取出新进程执行
void OperatingSystem::RequestDisk(const int &disk_number, int track_no)
{
    if ((disk_number < number_of_hard_disks) && (disk_number >= 0))
    {
        // 检查当前进程是否为系统进程（进程1不能使用磁盘）
        if (CPU == 1)
        {
            std::cout << "系统进程（PID = 1）不能请求磁盘。" << std::endl;
            return;
        }

        // 获取当前使用CPU的进程
        PCB* cpu_process = all_processes[CPU];

        // 请求硬盘：将进程添加到指定硬盘的I/O队列中
        hard_disks[disk_number]->Request(track_no, CPU);

        // 从CPU中移除当前进程
        CPU = -1;

        // 从就绪队列中取出下一个进程并分配给CPU
        GetNextFromReadyQueue();

        std::cout << "进程 " << cpu_process->GetPid() << " 请求访问硬盘 " << disk_number
            << " 的磁道 " << track_no << "。" << std::endl;
        std::cout << "当前使用CPU的进程为：" << CPU << std::endl;
    }
    else
    {
        std::cout << "没有硬盘" << disk_number << std::endl;
    }
}

// 如果就绪队列包含给定进程，则从就绪队列中删除该进程。 
void OperatingSystem::RemoveFromReadyQueue(const int pid_)
{
    auto pid_to_delete = std::find(ready_queue.begin(), ready_queue.end(), pid_);
    // 如果在就绪队列中，则删除它。 
    if (pid_to_delete != ready_queue.end())
    {
        ready_queue.erase(pid_to_delete);
    }
}

// 检查每个硬盘上是否有给定进程，如果该进程正在使用磁盘并位于其中一个 IO队列上，则会将其删除。
void OperatingSystem::RemoveFromDisks(const int &pid_)
{
    for (auto itr = hard_disks.begin(); itr != hard_disks.end(); itr++)
    {
        (*itr)->Remove(pid_);
    }
}

// 显示当前使用 CPU 的进程，并列出就绪队列中的所有进程。
void OperatingSystem::Snapshot() const
{
    std::cout << "占用CPU的进程：" << CPU << std::endl;
    std::cout << "就绪队列";
    for (auto itr = ready_queue.begin(); itr != ready_queue.end(); ++itr)
    {
        std::cout << " <- " << *itr;
    }
    std::cout << std::endl;
}

// 显示内存状态
// 对于每个使用的页框，显示占用它的进程号以及其中存储的页面号。
// 页面和页框的编号从0开始
void OperatingSystem::MemorySnapshot()
{
    std::cout << "页框\t\t"
              << "页面\t\t"
              << "pid\t\t"
              << "时间戳" << std::endl;
    // 遍历所有页框
    for (unsigned int i = 0; i < frames.size(); i++)
    {
        // 检查页框是否为空（指针是否有效）以及是否被占用
        if (frames[i] != nullptr && frames[i]->pid_ != 0)
        {
            std::cout << i << "\t\t"  // 显示页框号
                << frames[i]->page_ << "\t\t" // 显示页面号
                << frames[i]->pid_ << "\t\t"  // 显示进程 PID
                << frames[i]->timestamp_ << std::endl; // 显示时间戳
        }
    }
}

// 显示哪些进程当前正在使用硬盘以及哪些进程正在等待使用它们
void OperatingSystem::IOSnapshot() const
{
    for (int i = 0; i < number_of_hard_disks; i++)
    {
        std::cout << "硬盘 " << i << ": ";
        if (hard_disks[i]->DiskIsIdle())
        {
            std::cout << "空闲" << std::endl;
        }
        else
        {
            std::cout << "[" << hard_disks[i]->GetCurrentProcess() << " " << hard_disks[i]->GetCurrentTrack() << "]" << std::endl;
            std::cout << "硬盘 " << i << "的IO队列: ";
            hard_disks[i]->PrintQueue();
        }
    }
}

// 从CPU中移除当前进程并从就绪队列中取出新进程执行 
void OperatingSystem::GetNextFromReadyQueue()
{
    if (ready_queue.empty())
    {
        CPU = 1;
    }
    else
    {
        CPU = ready_queue.front();
        ready_queue.pop_front();
    }
}

// 当完成一个磁盘IO后，将其进程放回到就绪队列中。
void OperatingSystem::RemoveProcessFromDisk(const int &disk_number)
{
    if ((disk_number < number_of_hard_disks) && (disk_number >= 0))
    {
        int removed_pcb = hard_disks[disk_number]->RemoveProcess();
        AddToReadyQueue(removed_pcb);
    }
    else
    {
        std::cout << "没有硬盘" << disk_number << std::endl;
    }
}

// 搜索给定进程的每个页框。如果找到该进程，则将其删除。
void OperatingSystem::RemoveFromFrames(const int &pid)
{
    for (auto itr = frames.begin(); itr != frames.end(); itr++)
    {
        if ((*itr)->pid_ == pid)
        {
            (*itr)->Clear();
        }
    }
}

// 删除进程的所有子进程，并从所有的磁盘、页框、IO队列和就绪队列中删除它们及其PCB。
void OperatingSystem::DeleteChildren(PCB *pcb)
{
    // 删除当前的进程的所有子进程 
    for (auto itr = pcb->GetChildren().begin(); itr != pcb->GetChildren().end(); itr++)
    {
        PCB *child = (*itr);
        if (child->HasChildren())
        {
            DeleteChildren(child);
        }
        else
        {
            RemoveFromDisks(child->GetPid());
            RemoveFromFrames(child->GetPid());
            RemoveFromReadyQueue(child->GetPid());

            // 从PCB表中移除子进程
            all_processes.erase(child->GetPid());
            delete child;
            child = nullptr;
        }
    }

    pcb->ClearChildren();

    // 保持当前进程的僵死状态并稍后删除PCB
    if (!pcb->IsZombieProcess())
    {
        all_processes.erase(pcb->GetPid());
    }

    RemoveFromDisks(pcb->GetPid());
    RemoveFromFrames(pcb->GetPid());
    RemoveFromReadyQueue(pcb->GetPid());
}
