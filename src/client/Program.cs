using System.Security.Authentication;
using System.Security.Cryptography.X509Certificates;
using System.Text.Json;
using System.Text;
using System.Net.Http.Headers;

public class Program
{
    public static void Main()
    {
        // Load the client certificate
        X509Certificate2 clientCertificate = new X509Certificate2("D:\\Projects\\tlsdemo\\ssl\\client\\client_pavel.p12", "123456");

        // Create a HttpClientHandler with self-sign certificate
        var handler = new HttpClientHandler();
        handler.ClientCertificateOptions = ClientCertificateOption.Manual;
        handler.ServerCertificateCustomValidationCallback =
            (httpRequestMessage, cert, cetChain, policyErrors) =>
            {
                return true;
            };
        handler.SslProtocols = SslProtocols.Tls12;
        handler.ClientCertificates.Add(clientCertificate);

        // Create a HttpClient with MTLS authentication
        HttpClient httpClient = new HttpClient(handler);
        httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/string"));
        httpClient.Timeout = TimeSpan.FromSeconds(30);

        // Create the payload
        using StringContent jsonContent = new(
        JsonSerializer.Serialize(new
        {
            userId = 77,
            id = 1,
            title = "write code sample",
            completed = false
        }),
        Encoding.UTF8,
        "application/json");

        // Send the webhook request
        HttpResponseMessage response = httpClient.PostAsync("https://10.39.101.186:8443/csexim", jsonContent).Result;

        if (response.IsSuccessStatusCode)
        {
            string result = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine(result);
        }
        else
        {
            // Handle the error scenario
        }

    }

}