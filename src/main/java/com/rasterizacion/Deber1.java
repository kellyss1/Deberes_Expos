package com.rasterizacion;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;

public class Deber1 {

    private long window;

    public void run() {

        if (!glfwInit()) {
            throw new IllegalStateException("No se pudo iniciar GLFW");
        }

        window = glfwCreateWindow(800, 600, "Deber 1: Interseccion 3D y Z-Buffer", 0, 0);

        if (window == 0) {
            throw new RuntimeException("No se pudo crear la ventana");
        }

        glfwMakeContextCurrent(window);

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            glViewport(0, 0, width, height);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            float aspectRatio = (float) width / (float) height;
            if (width >= height) {
                glFrustum(-aspectRatio, aspectRatio, -1, 1, 1, 100);
            } else {
                glFrustum(-1, 1, -1 / aspectRatio, 1 / aspectRatio, 1, 100);
            }
            glMatrixMode(GL_MODELVIEW);
        });

        glfwSwapInterval(1);
        GL.createCapabilities();

        init();
        loop();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void init() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetFramebufferSize(window, width, height);
        glViewport(0, 0, width[0], height[0]);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        float aspectRatio = (float) width[0] / (float) height[0];
        if (width[0] >= height[0]) {
            glFrustum(-aspectRatio, aspectRatio, -1, 1, 1, 100);
        } else {
            glFrustum(-1, 1, -1 / aspectRatio, 1 / aspectRatio, 1, 100);
        }

        glMatrixMode(GL_MODELVIEW);

        // =========================================================================
        // PRUEBA DEL Z-BUFFER:
        // Para la segunda parte del deber, comenta (pon //) en la siguiente línea:
        // =========================================================================
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        glClearColor(0.1f, 0.1f, 0.1f, 1f); // Fondo gris oscuro para resaltar colores
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            // Limpiamos tanto el buffer de color como el de profundidad (Z-Buffer)
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            dibujar();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void dibujar() {
        // CUBO ROJO (Desplazado ligeramente hacia la izquierda, abajo y más cerca)
        glLoadIdentity();
        glTranslatef(-0.4f, -0.3f, -5.0f);
        glRotatef(25f, 1.0f, 1.0f, 0.0f); // Rotación estática para apreciar las 3 dimensiones
        dibujarCuboSolido(1.0f, 0.1f, 0.1f, 1.2f);

        // CUBO AZUL (Desplazado hacia la derecha, arriba y un poco más atrás)
        glLoadIdentity();
        glTranslatef(0.4f, 0.3f, -5.8f);
        glRotatef(-20f, 0.0f, 1.0f, 0.0f); // Rotación estática diferente
        dibujarCuboSolido(0.1f, 0.3f, 1.0f, 1.2f);
    }

    /**
     * Dibuja un cubo sólido de 6 caras.
     * Se aplican ligeras variaciones de brillo del color base a cada cara
     * para que la geometría 3D sea discernible sin usar un sistema de iluminación real.
     */
    private void dibujarCuboSolido(float r, float g, float b, float tamano) {
        float d = tamano / 2.0f; // Distancia del centro a las caras

        glBegin(GL_QUADS);

        // Cara Frontal (Color base)
        glColor3f(r, g, b);
        glVertex3f(-d, -d,  d);
        glVertex3f( d, -d,  d);
        glVertex3f( d,  d,  d);
        glVertex3f(-d,  d,  d);

        // Cara Trasera (Un poco más oscura)
        glColor3f(r * 0.7f, g * 0.7f, b * 0.7f);
        glVertex3f(-d, -d, -d);
        glVertex3f(-d,  d, -d);
        glVertex3f( d,  d, -d);
        glVertex3f( d, -d, -d);

        // Cara Superior (Más clara)
        glColor3f(Math.min(r * 1.2f, 1f), Math.min(g * 1.2f, 1f), Math.min(b * 1.2f, 1f));
        glVertex3f(-d,  d, -d);
        glVertex3f(-d,  d,  d);
        glVertex3f( d,  d,  d);
        glVertex3f( d,  d, -d);

        // Cara Inferior
        glColor3f(r * 0.5f, g * 0.5f, b * 0.5f);
        glVertex3f(-d, -d, -d);
        glVertex3f( d, -d, -d);
        glVertex3f( d, -d,  d);
        glVertex3f(-d, -d,  d);

        // Cara Derecha
        glColor3f(r * 0.85f, g * 0.85f, b * 0.85f);
        glVertex3f( d, -d, -d);
        glVertex3f( d,  d, -d);
        glVertex3f( d,  d,  d);
        glVertex3f( d, -d,  d);

        // Cara Izquierda
        glColor3f(r * 0.6f, g * 0.6f, b * 0.6f);
        glVertex3f(-d, -d, -d);
        glVertex3f(-d, -d,  d);
        glVertex3f(-d,  d,  d);
        glVertex3f(-d,  d, -d);

        glEnd();
    }

    public static void main(String[] args) {
        new Deber1().run();
    }
}
