/* =======================
   C++ SECTION
   ======================= */
#ifndef BUILD_AS_C
#include <iostream>

// Declare the C function with C linkage so C++ can link with it
extern "C" void cfunc(void);

int main() {
    std::cout << "Calling C code..." << std::endl;
    cfunc();
    return 0;
}

#endif // BUILD_AS_C

/* =======================
   C SECTION
   ======================= */
#ifdef BUILD_AS_C
#include <stdio.h>

// Forward declarations fix ordering errors when parsed directly as a .c file
int main_(void);
void nested_function(void);

void cfunc(void) {
    main_();
    printf("%d\n", main_());
}

int main_(void) {
    // Variable Length Array (VLA) - valid in C99
    int n = 5;
    int arr[n]; 

    for (int i = 0; i < n; i++) {
        arr[i] = i * 2;
    }

    // C99 compound literal
    int *p = (int[]){1, 2, 3, 4, 5};

    printf("VLA contents: ");
    for (int i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");

    printf("Compound literal first element: %d\n", p[0]);

    nested_function();

    return 0;
}

void nested_function(void) {
    printf("Hello from nested function!\n");
}

#endif // BUILD_AS_C