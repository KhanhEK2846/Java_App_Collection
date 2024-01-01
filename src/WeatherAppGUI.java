import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

public class WeatherAppGUI extends JFrame{

    private JSONObject weatherData;

    public WeatherAppGUI(){
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650); //pixel
        setLocationRelativeTo(null);//center screen
        setLayout(null);
        setResizable(false);//prevent resize
        addGuiComponents();
    }
    private void addGuiComponents(){
        //Search field
        JTextField searchTextField = new JTextField();

        //locate & size
        searchTextField.setBounds(15,15,351,45);

        //font style & size
        searchTextField.setFont(new Font("Dialog",Font.PLAIN,24));
        add(searchTextField);

        //Weather image
        JLabel waetherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        waetherConditionImage.setBounds(0,125,450,217);
        add(waetherConditionImage);

        //Temperature
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0,350,450,54);
        temperatureText.setFont(new Font("Dialog",Font.BOLD,48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);
                
        //Weather Condition
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0,405,450,36);
        weatherConditionDesc.setFont(new Font("Dialog",Font.PLAIN,32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);
        
        //Humidity image
        JLabel HumidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        HumidityImage.setBounds(15,500,74,66);
        add(HumidityImage);
        
        //Humidity text
        JLabel HumidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        HumidityText.setBounds(90,500,85,55);
        HumidityText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(HumidityText);

        //Wind sppeed image
        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220,500,74,66);
        add(windspeedImage);
        
        //Wind speed text
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(310,500,85,55);
        windspeedText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(windspeedText);

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        //cursor
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,13,47,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String UserInput = searchTextField.getText();
                if(UserInput.replaceAll("\\s", "").length() <= 0){
                    return;
                }
                weatherData = WeatherApp.getWeatherDAta(UserInput);

                String weatherCondition = (String)weatherData.get("weather_condition");
                switch(weatherCondition){
                    case "Clear":
                        waetherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        waetherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        waetherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        waetherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                // update temperature text
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + " C");

                // update weather condition text
                weatherConditionDesc.setText(weatherCondition);

                // update humidity text
                long humidity = (long) weatherData.get("humidity");
                HumidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                // update windspeed text
                double windspeed = (double) weatherData.get("wind_speed");
                windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");

            }
        });
        add(searchButton);
    }

    //create image components
    private ImageIcon loadImage(String ScrPath){
        try{
            //Read Image from path
            BufferedImage image = ImageIO.read(new File(ScrPath));
            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Not Found");
        return null;
    }
}
