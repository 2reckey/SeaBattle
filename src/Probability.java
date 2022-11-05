public class Probability {
    int[][] status_field;
    int[][] probability_field;
    int dimension;
    int[] quantity_ships;

    public Probability(Field field) {
        dimension = field.dimension;
        status_field = new int[dimension][dimension];
        probability_field = new int[dimension][dimension];
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                status_field[i][j] = field.position[i][j].status;
                if (status_field[i][j] > 0) {
                    status_field[i][j] = 0;
                }
            }
        }
        quantity_ships = CountShips(field.ship_array);
        CreateDeadShips(field.ship_array);
    }

    public Probability(int[][] status_field, int[] quantity_ships) {
        dimension = status_field.length;
        this.status_field = new int[dimension][dimension];
        this.quantity_ships = new int[quantity_ships.length];
        for (int i = 0; i < quantity_ships.length; ++i) {
            this.quantity_ships[i] = quantity_ships[i];
        }
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                this.status_field[i][j] = status_field[i][j];
            }
        }
    }

    public void ShowStatusField() {
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                System.out.print("| " + (status_field[i][j] >= 0 ? " " : "") + status_field[i][j] + "  " + ((j + 1) == dimension ? "|\n" : ""));
            }
        }
        System.out.println();
    }

    public void ShowProbabilityField() {
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                System.out.print("| " + (probability_field[i][j] >= 0 ? " " : "") + probability_field[i][j] + "  " + ((j + 1) == dimension ? "|\n" : ""));
            }
        }
        System.out.println();
    }

    public int[] CountShips(Ship[] ship_array) {
        int[] count_ships_array;
        int max_ship_dimension = 0;
        for (Ship ship : ship_array) {
            if (ship.dimension > max_ship_dimension) {
                max_ship_dimension = ship.dimension;
            }
        }
        count_ships_array = new int[max_ship_dimension];
        for (int i = 0; i < max_ship_dimension; ++i) {
            for (Ship ship : ship_array) {
                if (ship.dimension == i + 1) {
                    count_ships_array[i] += 1;
                }
            }
        }
        return count_ships_array;
    }

    public void CreateDeadShips(Ship[] ship_array) {
        Ship[] dead_ships;
        int count_dead_ships = 0;
        for (Ship ship : ship_array) {
            if (ship.status == 0) {
                ++count_dead_ships;
            }
        }
        dead_ships = new Ship[count_dead_ships];
        count_dead_ships = 0;
        for (Ship ship : ship_array) {
            if (ship.status == 0) {
                dead_ships[count_dead_ships] = ship;
                ++count_dead_ships;
            }
        }
        for (Ship dead_ship : dead_ships) {
            AddShip(dead_ship.ship_elements[0].position, dead_ship.ship_elements[dead_ship.dimension - 1].position);
            --quantity_ships[dead_ship.dimension - 1];
        }
    }

    public boolean CanAdd(int[] start_pos, int[] end_pos) {
        boolean cells_are_free = false;
        boolean ship_is_dead = false;
        boolean all_cells_in_broken = false;
        if ((start_pos[0] < dimension) && (start_pos[1] < dimension) && (start_pos[0] >= 0) && (start_pos[1] >= 0) && (end_pos[0] < dimension) && (end_pos[1] < dimension) && (end_pos[0] >= 0) && (end_pos[1] >= 0)) {
            int change = (start_pos[0] == end_pos[0] ? 1 : 0);
            int ship_dimension = Math.abs(end_pos[change] - start_pos[change]) + 1;
            int sgn = (end_pos[change] - start_pos[change]) / ((ship_dimension - 1 == 0 ? 1 : ship_dimension - 1));
            cells_are_free = true;
            ship_is_dead = true;
            all_cells_in_broken = true;
            int status;
            for (int i = 0; i < ship_dimension; ++i) {
                status = status_field[start_pos[0] + (change == 0 ? (sgn * i) : 0)][start_pos[1] + (change == 0 ? 0 : (sgn * i))];
                cells_are_free &= (status == 0) || (status == -2);
                ship_is_dead &= (status == -3 * ship_dimension);
                all_cells_in_broken &= (status == -2);
            }
        }
        return ((cells_are_free || ship_is_dead) && !all_cells_in_broken);
    }

    public void AddShip(int[] start_pos, int[] end_pos) {
        int change = (start_pos[0] == end_pos[0] ? 1 : 0);
        int ship_dimension = Math.abs(end_pos[change] - start_pos[change]) + 1;
        int sgn = (end_pos[change] - start_pos[change]) / ((ship_dimension - 1 == 0 ? 1 : ship_dimension - 1));
        for (int i = 0; i < ship_dimension; ++i) {
            status_field[start_pos[0] + (change == 0 ? (sgn * i) : 0)][start_pos[1] + (change == 0 ? 0 : (sgn * i))] = ship_dimension;
        }
    }

    public void GenerateRandomShips() {
        int[][] old_status_field = new int[dimension][dimension];
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                old_status_field[i][j] = status_field[i][j];
            }
        }
        int[] random_start_cords;
        int[] random_end_cords;
        int random_change;
        int random_sqn;
        int random_ship_dim;
        int count_break;
        out:
        {
            for (int i = 0; i < dimension; ++i) {
                for (int j = 0; j < dimension; ++j) {
                    if (status_field[i][j] == -2) {
                        count_break = 10 * dimension;
                        do {
                            --count_break;
                            random_ship_dim = Field.RandomInt(1, quantity_ships.length - 1);
                            random_sqn = (Field.RandomInt(0, 1) == 0 ? -1 : 1);
                            random_start_cords = new int[]{i, j};
                            random_change = Field.RandomInt(0, 1);
                            random_end_cords = new int[]{random_start_cords[0] + (random_change == 0 ? (random_sqn * random_ship_dim) : 0), random_start_cords[1] + (random_change == 0 ? 0 : (random_sqn * random_ship_dim))};
                            if (count_break == 0) {
                                for (int i_break = 0; i_break < dimension; ++i_break) {
                                    for (int j_break = 0; j_break < dimension; ++j_break) {
                                        status_field[i_break][j_break] = old_status_field[i_break][j_break];
                                    }
                                }
                                break out;
                            }
                        } while ((quantity_ships[random_ship_dim] < 1) || !(CanAdd(random_start_cords, random_end_cords)));
                        AddShip(random_start_cords, random_end_cords);
                        --quantity_ships[random_ship_dim];
                    }
                }
            }
            for (int i = quantity_ships.length - 1; i >= 0; --i) {
                for (int j = 0; j < quantity_ships[i]; ++j) {
                    count_break = 10 * dimension;
                    do {
                        --count_break;
                        random_start_cords = new int[]{Field.RandomInt(0, dimension - 1), Field.RandomInt(0, dimension - 1)};
                        random_change = Field.RandomInt(0, 1);
                        random_end_cords = new int[]{random_start_cords[0] + (random_change == 0 ? i : 0), random_start_cords[1] + (random_change == 0 ? 0 : i)};
                        if (count_break == 0) {
                            for (int i_break = 0; i_break < dimension; ++i_break) {
                                for (int j_break = 0; j_break < dimension; ++j_break) {
                                    status_field[i_break][j_break] = old_status_field[i_break][j_break];
                                }
                            }
                            break out;
                        }
                    } while (!CanAdd(random_start_cords, random_end_cords));
                    AddShip(random_start_cords, random_end_cords);
                }
            }
        }
    }

    public void GetProbabilityField(int accuracy) {
        Probability random_field;
        for (int k = 0; k < accuracy; ++k) {
            random_field = new Probability(status_field, quantity_ships);
            random_field.GenerateRandomShips();
            for (int i = 0; i < dimension; ++i) {
                for (int j = 0; j < dimension; ++j) {
                    if (random_field.status_field[i][j] > 0 && status_field[i][j] < 1 && status_field[i][j] != -2) {
                        ++probability_field[i][j];
                    }
                }
            }
        }
    }

    public int[] GetMaxProbabilityPos(int accuracy) {
        int[] max_pos = new int[2];
        int max = 0;
        GetProbabilityField(accuracy);
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (probability_field[i][j] > max) {
                    max = probability_field[i][j];
                    max_pos = new int[]{i, j};
                }
            }
        }
        if (max == 0) {
            do {
                max_pos = new int[]{Field.RandomInt(0, dimension - 1), Field.RandomInt(0, dimension - 1)};
            } while (status_field[max_pos[0]][max_pos[1]] != 0);
        }
        return max_pos;
    }
}
