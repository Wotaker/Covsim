using Plots: display
using DataFrames, Plots, CSV, Polynomials

#=
Poniższy skrypt estymuje R0 metodą opisaną tutaj:
https://en.wikipedia.org/wiki/Basic_reproduction_number#Estimation_methods
R0 powinno być wyznaczane na podstawie danych z początku pandemii, bo tylko wtedy założenie o tym,
że prawie wszyscy są podatni na infekcję jest spełnione, przez co wzrost liczby zainfekowanych jest
wykładniczy. Można to zagwarantować dobierając odpowiednio 'range', tak aby wykres logCasesPlot 
jak najbardziej przypominał liniowy. Wydaje się jednak że metoda estymacji R0 w ten sposób jest
niedokładna, ponieważ Ciężko jest spełnić powyższe założenie o stosunku Suspectible bliskim 1,
a samo R0 wychodzi zmienne dla zmiennych przedziałów
=#

fileCSVname = "DataSEIR.csv"
data = DataFrame(CSV.File("./savedData/$fileCSVname"))
N = sum(data[1, 2:3])
range = 40:70

cases = map(n -> N - n, data[range, :Suspectible])
casesPlot = plot(
  data[range, :Day],
  cases,
  title = "Total Cases",
  xlabel = "Day",
  ylabel = "Cases",
  label = "linScale",
)

logCases = map(n -> log(n), cases)
logCasesPlot = plot(
  data[range, :Day],
  logCases,
  title = "Total Cases in log scale",
  xlabel = "Day",
  ylabel = "Cases",
  label = "logScale",
)
combined = plot(casesPlot, logCasesPlot)
display(logCasesPlot)
display(casesPlot)
display(combined)

k = fit(data[range, :Day], logCases, 1)[1]
t = last(range) - range[1]
exp(k * t)
