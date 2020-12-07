using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
public class Navigation : MonoBehaviour
{
    public Button startButton;
    public AudioSource bgMusic;
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void GoToMenu()
    {
        //Initiate.Fade("Menu", Color.white, 3.0f);
        startButton.gameObject.SetActive(false);
    }


}
