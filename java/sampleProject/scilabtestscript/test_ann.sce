
layers = [2,2,1];

in = [0.9,0.8;
 0.7, 0.9;
 0.2, 0.1;
]';

out = [1, 1, 0];

speed_coeff = [0.5, 0];

weights = ann_FF_init(layers);
train_cycles = 400;

weights = ann_FF_Std_online(in, out, layers, weights, speed_coeff, train_cycles);

result = ann_FF_run(in(:, 1), layers, weights);


