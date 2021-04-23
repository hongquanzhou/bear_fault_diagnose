#ifndef PROPERTIES_H
#define PROPERTIES_H
#include <map>
#include <vector>
#include <fstream>
using namespace std;
class Properties
{
    private:
        map<string,string> Map;
        vector<string> spilt(char* line);
    public:
        Properties(const char* filename);
        string getProperties(string key);
};
#endif
