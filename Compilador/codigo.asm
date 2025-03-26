section .data
    msgGreater db "El numero es mayor a 10", 0
    msgSmaller db "El numero es menor o igual a 10", 0
    newline db 0x0D, 0x0A, 0
    number dw 5   ; Declarar y asignar valor a number

section .text
    global _start
_start:

    ; Fin del programa
    mov ax, 4C00h  ; Terminar el programa
    int 21h
