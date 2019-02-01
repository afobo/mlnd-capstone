# Machine Learning Engineer Nanodegree
## Capstone Proposal
Sergey Sergeev  
December 31st, 2050

## Proposal
_(approx. 2-3 pages)_

### Domain Background

I work for the software development company, and we pay high attention to the test automation.
During 15 years of our product development and many implemented customer projects we accumulated a lot of autotests.
The autotests checks various parts of our product in different environments, e.g. 
- 1, 2, 4, 8 virtual machine clusters
- RedHat Enterprise Linux 6 or 7
- different versions of Java (our product is written mostly in Java)

Individual test runtime vary from few minutes to several hours depending on their complexity and size.

We built the private cloud (based on Openstack) for the continuous autotest execution and a simple Web interface to manage it.
On the dedicated page test engineer or developer may choose the list of tests he want to execute, number of virtual machines, versions of the 3rdparty software to be provisioned in the VMs, and so on.
After that the execution request is queued and will be executed as soon as possible according to cloud capacity and current VM consumption.
As a result, engineer receives an email with the test report.
Test reports are also stored in some dedicated place for reference, comparison and further analysis needs.     

### Problem Statement

For the higher cloud capacity utilization and better user experience we would like to have a model capable to predict overall test execution time.
This time is a sum of three:
- **Queue time (t1)**: time spent by an execution request in the queue waiting for the free cloud capacity
- **VM preparation time (t2)**: time to create a cluster of VMs and provision it with the requested 3rdparty software
- **Actual test execution time (t3)**: actual test execution till the final report   

In this project, I would focus on t2 estimation only, leaving t1 and t3 for a future.

### Datasets and Inputs

As an input, I would use historical data of 1200 VM preparations requests collected over 2 months.

- Requests : 1200 | 5496 VMs
-    Daily : 17.1 | 78.5 VMs
-  Success : 1192 | 99.33%
-  Failure :    8 | 0.66%

For a single request, I have the following data:
- Start timestamp
- End timestamp
- Number of VMs
- VM flavor (# of CPUs, RAM and Disk)
- Base OS version: RHEL 6.9, 7.3 or 7.4
- Requested 3rdparty software (with version numbers)
- Success/Failure flag
- Number of attempts performed to create and provision VMs

### Solution Statement
_(approx. 1 paragraph)_

In this section, clearly describe a solution to the problem. The solution should be applicable to the project domain and appropriate for the dataset(s) or input(s) given. Additionally, describe the solution thoroughly such that it is clear that the solution is quantifiable (the solution can be expressed in mathematical or logical terms) , measurable (the solution can be measured by some metric and clearly observed), and replicable (the solution can be reproduced and occurs more than once).

### Benchmark Model
_(approximately 1-2 paragraphs)_

In this section, provide the details for a benchmark model or result that relates to the domain, problem statement, and intended solution. Ideally, the benchmark model or result contextualizes existing methods or known information in the domain and problem given, which could then be objectively compared to the solution. Describe how the benchmark model or result is measurable (can be measured by some metric and clearly observed) with thorough detail.

### Evaluation Metrics
_(approx. 1-2 paragraphs)_

In this section, propose at least one evaluation metric that can be used to quantify the performance of both the benchmark model and the solution model. The evaluation metric(s) you propose should be appropriate given the context of the data, the problem statement, and the intended solution. Describe how the evaluation metric(s) are derived and provide an example of their mathematical representations (if applicable). Complex evaluation metrics should be clearly defined and quantifiable (can be expressed in mathematical or logical terms).

### Project Design
_(approx. 1 page)_

In this final section, summarize a theoretical workflow for approaching a solution given the problem. Provide thorough discussion for what strategies you may consider employing, what analysis of the data might be required before being used, or which algorithms will be considered for your implementation. The workflow and discussion that you provide should align with the qualities of the previous sections. Additionally, you are encouraged to include small visualizations, pseudocode, or diagrams to aid in describing the project design, but it is not required. The discussion should clearly outline your intended workflow of the capstone project.

-----------

**Before submitting your proposal, ask yourself. . .**

- Does the proposal you have written follow a well-organized structure similar to that of the project template?
- Is each section (particularly **Solution Statement** and **Project Design**) written in a clear, concise and specific fashion? Are there any ambiguous terms or phrases that need clarification?
- Would the intended audience of your project be able to understand your proposal?
- Have you properly proofread your proposal to assure there are minimal grammatical and spelling mistakes?
- Are all the resources used for this project correctly cited and referenced?