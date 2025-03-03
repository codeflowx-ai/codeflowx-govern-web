// gradio.js
console.log("Definiendo funciones de Gradio");

window.gradioInitialized = false;

window.startASR = function() {
    console.log("Iniciando ASR...");
    if (window.gradioInstance) {
        console.log("Gradio instance encontrado. Iniciando...");
        window.gradioInstance.run();
    } else {
        console.error("Gradio aún no se ha inicializado. Intentando nuevamente...");
        setTimeout(window.startASR, 1000);
    }
};

window.loadGradio = function() {
    if (window.gradioLoading) {
        console.log("Gradio ya está cargando...");
        return;
    }
    
    window.gradioLoading = true;
    console.log("Intentando cargar el script de Gradio...");
    
    let script = document.createElement("script");
    script.src = "https://gradio.s3-us-west-2.amazonaws.com/3.50.2/gradio.js";

    script.onload = () => {
        console.log("Gradio.js cargado correctamente.");
        // Asegurarse de que gradio esté definido
        if (typeof gradio !== 'undefined') {
            gradio.load("https://huggingface.co/spaces/gradio/real-time-speech-recognition", "#asr-container")
                .then(gr => {
                    console.log("Gradio ha sido inicializado.");
                    window.gradioInstance = gr;
                    window.gradioLoading = false;

                    gr.on('data', (text) => {
                        console.log("Texto transcrito recibido:", text);
                        let transcriptionBox = zk.Widget.$("$transcriptionBox");
                        if (transcriptionBox) {
                            transcriptionBox.setValue(text);
                        }
                    });
                })
                .catch(err => {
                    console.error("Error al cargar Gradio desde Hugging Face:", err);
                    window.gradioLoading = false;
                });
        } else {
            console.error("Gradio no está definido después de cargar el script");
            window.gradioLoading = false;
        }
    };

    script.onerror = () => {
        console.error("Error al cargar el script de Gradio.");
        window.gradioLoading = false;
    };

    document.head.appendChild(script);
};

console.log("Funciones de Gradio definidas");