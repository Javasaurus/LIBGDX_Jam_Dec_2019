#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform float flash;

void main() {
        vec4 color = texture2D(u_texture, v_texCoords).rgba;
        if(flash>0){
            gl_FragColor = vec4(1,color.g,color.b,color.a);
        }else{
            gl_FragColor = color;
        }
 
}