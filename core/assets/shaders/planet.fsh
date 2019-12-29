#ifdef GL_ES
#define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;
uniform float time;
uniform float flash;
uniform float grayness;
#define pi 3.14159265

void main() {

    float x = 2.0 * (v_texCoord0.x - 0.5);
    float y = 2.0 * (v_texCoord0.y - 0.5);
    float r = sqrt(x*x + y*y);

    float d = 0.0;
    if(r != 0.0) {
        d = asin(r) / r;
    }

    float x2 = d * x;
    float y2 = d * y;

    float x3 = mod(x2 / (2.0 * pi) + 0.5 + time, 1.0);
    float y3 = y2 / (2.0 * pi) + 0.5;

    if(r <= 1.0) {
        if(flash>0){
            gl_FragColor = vec4(1, 1, 1, 1);
        }else{
            vec4 c = texture2D(u_sampler2D, vec2(x3, y3)) * v_color;
            float grey = (c.r + c.g + c.b)/3.0;
            vec3 blend = mix(c.rgb,vec3(grey),grayness);
            gl_FragColor = vec4(blend.rgb,c.a);
        }
    } else {
        gl_FragColor = vec4(0, 0, 0, 0);
    }
}
