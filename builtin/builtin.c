//内置函数的语言实现
//利用库直接将c转为LLVM IR

//启用bool
#define bool _Bool

//声明借用的libc函数
int printf(const char *pattern, ...);
int sprintf(char *dest, const char *pattern, ...);
int scanf(const char *pattern, ...);
int sscanf(const char *src, const char *pattern, ...);
int strlen(const char *str);
int strcmp(const char *s1, const char *s2);
char *strcpy(char *dest, const char *src);
char *strcat(char *dest, const char *src);
void *memcpy(void *dest, const void *src, int n);
void *malloc(int n);

//Mx内建函数
void print(char *str) { printf("%s", str); }

void println(char *str) { printf("%s\n", str); }

void printInt(int x) { printf("%d", x); }

void printlnInt(int x) { printf("%d\n", x); }

char *getString(){
    char *str = (char *) malloc(sizeof(char) * (1<<8));
    scanf("%s", str);
    return str;
}

int getInt() {
    int x;
    scanf("%d", &x);
    return x;
}

char *toString(int i) {
    char *str = (char *) malloc(sizeof(char) * (1<<8));
    sprintf(str, "%d", i);
    return str;
}

//array内建方法（将数组长度放在数组前4字节）
int _array_size(void *__this) {
    return ((int*)__this)[-1];
}
//string内建方法
int _string_length(char *str) {
         return strlen(str);
}

char *_string_substring(char *str, int left, int right){
    int len = right - left + 1;
    char *substr = (char *) malloc(sizeof(char) * len);
    memcpy(substr, str + left, len);
    substr[len - 1] = '\0';
    return substr;
}

int _string_parseInt(char *str){
    int num;
    sscanf(str, "%d", &num);
    return num;
}

int _string_ord(char *str, int pos){
    return str[pos];
}

//string运算实现
bool _string_equal(char *str1, char *str2) {
    return strcmp(str1, str2) == 0;
}

bool _string_notEqual(char *str1, char *str2) {
    return strcmp(str1, str2) != 0;
}

bool _string_less(char *str1, char *str2) {
    return strcmp(str1, str2) < 0;
}

bool _string_lessOrEqual(char *str1, char *str2) {
    return strcmp(str1, str2) <= 0;
}

bool _string_greater(char *str1, char *str2) {
    return strcmp(str1, str2) > 0;
}

bool _string_greaterOrEqual(char *str1, char *str2) {
    return strcmp(str1, str2) >= 0;
}

char *_string_add(char *str1, char *str2) {
    char *str = (char *) malloc(sizeof(char) * (strlen(str1) + strlen(str2) + 1));
    strcpy(str, str1);
    strcat(str, str2);
    return str;
}

//分配空间alloca
//按字节
//TODO：free？
char *_malloc(int size) {
    return (char *) malloc(size);
}